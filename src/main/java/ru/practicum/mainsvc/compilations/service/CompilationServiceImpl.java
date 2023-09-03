package ru.practicum.mainsvc.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.mainsvc.compilations.dto.CompilationDto;
import ru.practicum.mainsvc.compilations.dto.CompilationMapper;
import ru.practicum.mainsvc.compilations.dto.NewCompilationDto;
import ru.practicum.mainsvc.compilations.dto.UpdateCompilationRequest;
import ru.practicum.mainsvc.compilations.model.Compilation;
import ru.practicum.mainsvc.compilations.model.EventsCompilations;
import ru.practicum.mainsvc.compilations.model.EventsCompilationsId;
import ru.practicum.mainsvc.compilations.repository.CompilationRepository;
import ru.practicum.mainsvc.event.dto.EventShortDto;
import ru.practicum.mainsvc.event.service.EventService;
import ru.practicum.mainsvc.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventsCompilationsService eventsCompilationsService;
    private final EventService eventService;
    private final CompilationMapper compilationMapper;

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from, size);
        Page<Compilation> compilations;
        if (pinned == null) {
            compilations = compilationRepository.findAll(pageable);
        } else {
            compilations = compilationRepository.getCompilations(pinned, pageable);
        }

        List<CompilationDto> compilationDtoList = new ArrayList<>();
        for (Compilation compilation : compilations) {
            List<EventsCompilations> eventsCompilations =
                    eventsCompilationsService.getCompilation(compilation.getId());
            List<Long> eventIds = eventsCompilations.stream()
                    .map(EventsCompilations::getEventId)
                    .collect(Collectors.toList());
            List<EventShortDto> events = new ArrayList<>();
            if (!eventIds.isEmpty()) {
                events = eventService.getEventsListByIdsList(eventIds);
            }
            CompilationDto compilationDto = compilationMapper.mapToCompilationDto(compilation, events);
            compilationDtoList.add(compilationDto);
        }
        return compilationDtoList;
    }

    @Override
    public CompilationDto getCompilation(Long compId) {
        Compilation compilation = compilationRepository.getReferenceById(compId);

        List<EventsCompilations> eventsCompilations =
                eventsCompilationsService.getCompilation(compilation.getId());
        List<Long> eventIds = eventsCompilations.stream()
                .map(EventsCompilations::getEventId)
                .collect(Collectors.toList());
        List<EventShortDto> events = eventService.getEventsListByIdsList(eventIds);
        return compilationMapper.mapToCompilationDto(compilation, events);
    }

    @Override
    public CompilationDto saveCompilation(NewCompilationDto compilationDto) {
        List<Long> eventIds = compilationDto.getEvents();
        eventService.checkEventsExist(eventIds);
        compilationRepository.save(compilationMapper.mapNewDtoToCompilation(compilationDto));
        Compilation compilation = compilationRepository.getCompilationByTitle(compilationDto.getTitle());
        if (compilationDto.getPinned() == null) {
            compilationDto.setPinned(false);
        }
        for (Long eventId : eventIds) {
            eventsCompilationsService.saveEventCompilation(
                    new EventsCompilations(compilation.getId(), eventId)
            );
        }
        return getCompilation(compilation.getId());
    }

    @Override
    public void deleteCompilation(Long compId) {
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest request) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Не найдено"));

        if (request.getPinned() != null) {
            compilation.setPinned(request.getPinned());
        }
        if (request.getTitle() != null) {
            compilation.setTitle(request.getTitle());
        }
        Compilation compilationSaved = compilationRepository.saveAndFlush(compilation);

        if (request.getEvents() != null) {
            List<EventsCompilations> eventsComp = eventsCompilationsService.getCompilation(compId);
            List<EventsCompilationsId> ids = new ArrayList<>();
            for (EventsCompilations ec : eventsComp) {
                ids.add(new EventsCompilationsId(compId, ec.getEventId()));
            }
            eventsCompilationsService.deleteEventsCompilations(ids);

            List<EventsCompilations> ecList = new ArrayList<>();
            for (Long id : request.getEvents()) {
                ecList.add(new EventsCompilations(compId, id));
            }
            eventsCompilationsService.saveListOfEventsCompilations(ecList);
        }
        List<EventShortDto> eventShortDtos = eventService.getEventsListByIdsList(request.getEvents());

        return compilationMapper.mapToCompilationDto(compilationSaved, eventShortDtos);
    }

    @Override
    public void deleteEventFromCompilation(Long compId, Long eventId) {
        eventsCompilationsService.deleteEventFromCompilation(compId, eventId);
    }

    @Override
    public void unpinCompilation(Long compId) {
        Compilation compilation = compilationRepository.getReferenceById(compId);
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    public void pinCompilation(Long compId) {
        Compilation compilation = compilationRepository.getReferenceById(compId);
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }
}
