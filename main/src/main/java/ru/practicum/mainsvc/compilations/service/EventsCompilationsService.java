package ru.practicum.mainsvc.compilations.service;

import ru.practicum.mainsvc.compilations.model.EventsCompilations;
import ru.practicum.mainsvc.compilations.model.EventsCompilationsId;

import java.util.List;
import java.util.Map;

public interface EventsCompilationsService {
    List<EventsCompilations> getCompilation(Long compilationId);

    Map<Long, List<EventsCompilations>> getCompilationMap(List<Long> compilationIds);

    void saveEventCompilation(EventsCompilations eventsCompilations);

    void deleteEventFromCompilation(Long compId, Long eventId);

    void deleteEventsCompilations(List<EventsCompilationsId> ids);

    void saveListOfEventsCompilations(List<EventsCompilations> ecList);
}
