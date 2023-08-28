package ru.practicum.mainsvc.compilations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainsvc.compilations.model.EventsCompilations;
import ru.practicum.mainsvc.compilations.model.EventsCompilationsId;
import ru.practicum.mainsvc.compilations.repository.EventsCompilationsRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventsCompilationsServiceImpl implements EventsCompilationsService {
    private final EventsCompilationsRepository eventsCompilationsRepository;

    @Override
    public List<EventsCompilations> getCompilation(Long compilationId) {
        return eventsCompilationsRepository.getCompilationById(compilationId);
    }

    @Override
    public Map<Long, List<EventsCompilations>> getCompilationMap(List<Long> compilationIds) {
        return eventsCompilationsRepository.getCompilationsByIds(compilationIds).stream()
                        .collect(
                                Collectors.groupingBy(
                                        EventsCompilations::getCompilationId,
                                        Collectors.toList()));
    }

    @Override
    public void saveEventCompilation(EventsCompilations eventsCompilations) {
        eventsCompilationsRepository.save(eventsCompilations);
    }

    @Override
    public void deleteEventFromCompilation(Long compId, Long eventId) {
        eventsCompilationsRepository.delete(new EventsCompilations(compId, eventId));
    }

    @Override
    public void deleteEventsCompilations(List<EventsCompilationsId> ids) {
        eventsCompilationsRepository.deleteAllById(ids);
    }

    @Override
    public void saveListOfEventsCompilations(List<EventsCompilations> ecList) {
        eventsCompilationsRepository.saveAll(ecList);
    }
}
