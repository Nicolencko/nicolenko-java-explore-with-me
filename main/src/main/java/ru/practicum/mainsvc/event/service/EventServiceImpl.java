package ru.practicum.mainsvc.event.service;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.dto.HitDtoInput;
import ru.practicum.StatClient;
import ru.practicum.mainsvc.category.repository.CategoryRepository;
import ru.practicum.mainsvc.category.model.Category;
import ru.practicum.mainsvc.event.dto.EventMapper;
import ru.practicum.mainsvc.event.repository.EventRepository;
import ru.practicum.mainsvc.event.dto.*;
import ru.practicum.mainsvc.event.model.Event;
import ru.practicum.mainsvc.exception.BadRequestException;
import ru.practicum.mainsvc.request.dto.RequestMapper;
import ru.practicum.mainsvc.request.service.RequestService;
import ru.practicum.mainsvc.request.dto.ParticipationRequestDto;
import ru.practicum.mainsvc.request.model.Request;
import ru.practicum.mainsvc.user.repository.UserRepository;
import ru.practicum.mainsvc.user.model.User;
import ru.practicum.mainsvc.utils.QPredicates;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static ru.practicum.mainsvc.event.model.QEvent.event;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    private final RequestService requestService;
    private final RequestMapper requestMapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSS]");
    private final StatClient client;

    @Override
    public List<EventShortDto> getEvents(String ip,
                                         String text,
                                         List<Long> categories,
                                         Boolean paid,
                                         String rangeStart,
                                         String rangeEnd,
                                         Boolean onlyAvailable,
                                         String sort,
                                         Integer from,
                                         Integer size) {
        client.saveHit(new HitDtoInput(
                "ewm-main-service",
                "/events",
                ip,
                LocalDateTime.now().format(formatter)
        ));
        Predicate predicate = QPredicates.builder()
                .add(text, (event.annotation.containsIgnoreCase(String.valueOf(text))).or(event.description.containsIgnoreCase(String.valueOf(text))))
                .add(categories, event.category::in)
                .add(paid, event.paid::eq)
                .add(parseLocalDateTime(rangeStart, formatter), event.eventDate::goe)
                .add(parseLocalDateTime(rangeEnd, formatter), event.eventDate::lt)
                .add(onlyAvailable, event.participantLimit.gt(event.confirmedRequests)
                        .and(event.participantLimit.ne(0)).or(event.participantLimit.eq(0)))
                .buildAnd();

        Pageable pageable;
        Iterable<Event> eventList;
        if (sort != null && sort.equals("EVENT_DATE")) {
            pageable = PageRequest.of(from, size, Sort.by("eventDate"));
        } else if (sort != null && sort.equals("VIEWS")) {
            pageable = PageRequest.of(from, size, Sort.by("views"));
        } else {
            pageable = PageRequest.of(from, size);
        }
        if (predicate != null) {
            eventList = eventRepository.findAll(predicate, pageable);
        } else {
            eventList = eventRepository.findAll(pageable);
        }

        return StreamSupport.stream(eventList.spliterator(), false)
                .map(eventMapper::mapToShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventFullDto> getEvents(List<Long> users,
                                        List<String> states,
                                        List<Long> categories,
                                        String rangeStart,
                                        String rangeEnd,
                                        Integer from,
                                        Integer size) {
        Predicate predicate = QPredicates.builder()
                .add(users, event.initiator::in)
                .add(states, event.state::in)
                .add(categories, event.category::in)
                .add(parseLocalDateTime(rangeStart, formatter), event.eventDate::goe)
                .add(parseLocalDateTime(rangeEnd, formatter), event.eventDate::lt)
                .buildAnd();

        Pageable pageable = PageRequest.of(from, size);
        Iterable<Event> eventList;
        if (predicate != null) {
            eventList = eventRepository.findAll(predicate, pageable);
        } else {
            eventList = eventRepository.findAll(pageable);
        }
        return StreamSupport.stream(eventList.spliterator(), false)
                .map(eventMapper::mapToFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventShortDto> getEventsListByIdsList(List<Long> idList) {
        List<Event> eventList = eventRepository.getEventsListByIdList(idList);
        return eventList.stream()
                .map(eventMapper::mapToShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEvent(Long id, String ip) {
        Event event = eventRepository.findById(id).get();
        Category category = categoryRepository.getReferenceById(event.getCategory());
        User user = userRepository.getReferenceById(event.getInitiator());
        client.saveHit(new HitDtoInput(
                "ewm-main-service",
                "/events/" + id,
                ip,
                LocalDateTime.now().format(formatter)
        ));
        return eventMapper.mapToFullDto(event, category, user);
    }

    @Override
    public Event getEvent(Long eventId) {
        return eventRepository.getReferenceById(eventId);
    }

    @Override
    public List<EventShortDto> getEventsByUserId(Long userId, Integer from, Integer size) {
        Predicate predicate = QPredicates.builder()
                .add(userId, event.initiator::eq)
                .buildAnd();
        Pageable pageable = PageRequest.of(from, size);
        Page<Event> events = eventRepository.findAll(predicate, pageable);
        return StreamSupport.stream(events.spliterator(), false)
                .map(eventMapper::mapToShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEventByUser(Long userId, Long eventId, UpdateEventUserRequest updateEventRequest) {
        Event event = eventRepository.getReferenceById(eventId);
        if (event.getState().equals("PUBLISHED")) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Нельзя изменить опубликованное событие");
        }
        if ((event.getState().equals("PENDING") || event.getState().equals("CANCELED"))
                && event.getEventDate().isAfter(LocalDateTime.now())) {
            if (updateEventRequest.getAnnotation() != null) {
                event.setAnnotation(updateEventRequest.getAnnotation());
            }
            if (updateEventRequest.getCategory() != null) {
                event.setCategory(updateEventRequest.getCategory());
            }
            if (updateEventRequest.getDescription() != null) {
                event.setDescription(updateEventRequest.getDescription());
            }
            if (updateEventRequest.getEventDate() != null) {
                event.setEventDate(parseLocalDateTime(updateEventRequest.getEventDate(), formatter));
            }
            if (updateEventRequest.getLocation() != null) {
                event.setLocationLat(updateEventRequest.getLocation().getLat());
                event.setLocationLon(updateEventRequest.getLocation().getLon());
            }
            if (updateEventRequest.getPaid() != null) {
                event.setPaid(updateEventRequest.getPaid());
            }
            if (updateEventRequest.getParticipantLimit() != null) {
                event.setParticipantLimit(updateEventRequest.getParticipantLimit());
            }
            if (updateEventRequest.getTitle() != null) {
                event.setTitle(updateEventRequest.getTitle());
            }
            if (updateEventRequest.getStateAction().equals("CANCEL_REVIEW")) {
                event.setState("CANCELED");
            }
            if (updateEventRequest.getStateAction().equals("SEND_TO_REVIEW")) {
                event.setState("PENDING");
            }
        }
        eventRepository.save(event);
        return eventMapper.mapToFullDto(event);
    }

    @Override
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updatedEvent) {
        Event event = eventRepository.getReferenceById(eventId);
        if (updatedEvent.getAnnotation() != null) {
            event.setAnnotation(updatedEvent.getAnnotation());
        }
        if (updatedEvent.getCategory() != null) {
            event.setCategory(updatedEvent.getCategory());
        }
        if (updatedEvent.getDescription() != null) {
            event.setDescription(updatedEvent.getDescription());
        }
        if (updatedEvent.getEventDate() != null) {
            if (parseLocalDateTime(updatedEvent.getEventDate(), formatter).isBefore(LocalDateTime.now())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Дата уже наступила");
            }
            event.setEventDate(parseLocalDateTime(updatedEvent.getEventDate(), formatter));
        }
        if (updatedEvent.getLocation() != null) {
            event.setLocationLat(updatedEvent.getLocation().getLat());
            event.setLocationLon(updatedEvent.getLocation().getLon());
        }
        if (updatedEvent.getPaid() != null) {
            event.setPaid(updatedEvent.getPaid());
        }
        if (updatedEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updatedEvent.getParticipantLimit());
        }
        if (updatedEvent.getRequestModeration() != null) {
            event.setRequestModeration(updatedEvent.getRequestModeration());
        }
        if (Objects.equals(updatedEvent.getStateAction(), "PUBLISH_EVENT")) {
            if (event.getState().equals("PUBLISHED") || event.getState().equals("CANCELED")) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Событие уже опубликовано или отменено");
            }
            event.setState("PUBLISHED");
        }
        if (Objects.equals(updatedEvent.getStateAction(), "REJECT_EVENT")) {
            if (event.getState().equals("PUBLISHED")) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Событие уже опубликованоб отменить нельзя");
            }
            event.setState("CANCELED");
        }
        if (updatedEvent.getTitle() != null) {
            event.setTitle(updatedEvent.getTitle());
        }

        eventRepository.save(event);
        return eventMapper.mapToFullDto(event);
    }

    @Override
    public EventFullDto saveEvent(Long userId, NewEventDto newEventDto) {
        Event event = eventMapper.mapNewToEvent(newEventDto, userId);
        eventRepository.save(event);
        return eventMapper.mapToFullDto(event);
    }

    @Override
    public EventFullDto getEventByUserIdAndEventId(Long userId, Long eventId) {
        Event event = eventRepository.getReferenceById(eventId);
        if (!event.getInitiator().equals(userId)) {
            throw new BadRequestException("User " + userId + " not initiator");
        }
        return eventMapper.mapToFullDto(event);
    }

    @Override
    public List<ParticipationRequestDto> getEventParticipationByUserId(Long userId, Long eventId) {
        Event event = eventRepository.getReferenceById(eventId);
        if (!event.getInitiator().equals(userId)) {
            throw new BadRequestException("User " + userId + " not initiator");
        }
        List<Request> requests = requestService.getRequestsByEvent(eventId);
        return requests.stream()
                .map(requestMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult changeRequestsStatus(
            Long userId, Long eventId, EventRequestStatusUpdateRequest request) {
        Event event = eventRepository.getReferenceById(eventId);

        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);

        if (!event.getRequestModeration() || Objects.equals(request.getStatus(), "REJECTED")) {
            for (Long id : request.getRequestIds()) {
                Request request1 = requestService.getRequest(id);
                request1.setStatus("REJECTED");
                rejectedRequests.add(requestMapper.mapToDto(request1));
                requestService.saveRequest(request1);
            }
        }
        if (event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Лимит заявок исчерпан");
        }
        for (Long id : request.getRequestIds()) {
            Request request1 = requestService.getRequest(id);
            if (event.getConfirmedRequests() < event.getParticipantLimit() && event.getRequestModeration()) {
                request1.setStatus("CONFIRMED");
                confirmedRequests.add(requestMapper.mapToDto(request1));
                requestService.saveRequest(request1);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            } else {
                request1.setStatus("REJECTED");
                rejectedRequests.add(requestMapper.mapToDto(request1));
                requestService.saveRequest(request1);
            }
        }
        eventRepository.save(event);
        return result;
    }

    @Override
    public ParticipationRequestDto confirmRequest(Long userId, Long eventId, Long reqId) {
        Event event = eventRepository.getReferenceById(eventId);
        if (event.getParticipantLimit() == 0) {
            throw new BadRequestException("Подтверждение не требуется");
        }
        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests().equals(event.getParticipantLimit())) {
            throw new BadRequestException("Мест нет");
        }
        Request request = requestService.getRequestByReqId(eventId, reqId);
        request.setStatus("CONFIRMED");
        requestService.saveRequest(request);
        event.setConfirmedRequests(event.getConfirmedRequests() + 1);
        eventRepository.save(event);
        return requestMapper.mapToDto(request);
    }

    @Override
    public ParticipationRequestDto declineRequest(Long userId, Long eventId, Long reqId) {
        Request request = requestService.getRequestByReqId(eventId, reqId);
        request.setStatus("REJECTED");
        requestService.saveRequest(request);
        return requestMapper.mapToDto(request);
    }

    @Override
    public EventFullDto publishEvent(Long eventId) {
        Event event = eventRepository.getReferenceById(eventId);
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Ошибка при публикации события");
        }
        event.setState("PUBLISHED");

        eventRepository.save(event);
        return eventMapper.mapToFullDto(event);
    }

    @Override
    public EventFullDto rejectEvent(Long eventId) {
        Event event = eventRepository.getReferenceById(eventId);
        if (event.getState() == "PUBLISHED") {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Событие уже опубликовано");
        }
        event.setState("CANCELED");
        eventRepository.save(event);
        return eventMapper.mapToFullDto(event);
    }

    @Override
    public void checkEventsExist(List<Long> eventIds) {
        List<Integer> events = eventRepository.getAllIds();
        List<Long> eventList = events.stream().map(Long::valueOf).collect(Collectors.toList());
        for (Long event : eventIds) {
            if (!eventList.contains(event)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Event doesn't exist");
            }
        }
    }

    private static void saveHit(String ip, String id) {
        String body = "{\"app\":\"ewm-main-service\", \"uri\":\"/events" + id + "\", \"ip\":\"" + ip + "\"}";
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9090/hit"))
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString());


    }

    private static LocalDateTime parseLocalDateTime(CharSequence text, DateTimeFormatter formatter) {
        if (text == null) {
            return null;
        }
        return formatter.parse(text, LocalDateTime::from);
    }
}
