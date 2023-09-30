package ru.practicum.mainsvc.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainsvc.compilation.dto.CompilationDto;
import ru.practicum.mainsvc.compilation.dto.NewCompilationDto;
import ru.practicum.mainsvc.compilation.dto.UpdateCompilationDto;
import ru.practicum.mainsvc.compilation.mapper.CompilationMapper;
import ru.practicum.mainsvc.compilation.model.Compilation;
import ru.practicum.mainsvc.compilation.repository.CompilationRepository;
import ru.practicum.mainsvc.event.dto.EventShortDto;
import ru.practicum.mainsvc.event.model.Event;
import ru.practicum.mainsvc.event.repository.EventRepository;
import ru.practicum.mainsvc.event.service.EventService;
import ru.practicum.mainsvc.exception.NotFoundException;
import ru.practicum.mainsvc.util.Pagination;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;
    private final EventService eventService;

    @Override
    @Transactional
    public CompilationDto saveCompilation(NewCompilationDto compilationDto) {
        if (compilationDto.getPinned() == null) {
            compilationDto.setPinned(false);
        }
        Set<Event> events;
        if (compilationDto.getEvents() == null) {
            events = Collections.emptySet();
        } else {
            events = eventRepository.findByIdIn(compilationDto.getEvents());
        }

        Compilation compilation = compilationMapper.compilationFromNewDto(compilationDto, events);
        compilation = compilationRepository.saveAndFlush(compilation);
        Set<EventShortDto> eventShortDtos = eventService.getAllShortDto(compilationDto.getEvents());
        return compilationMapper.compilationToDto(compilation, eventShortDtos);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationDto compilationDto) {
        Compilation compilation = getOrThrow(compId);
        Set<Event> eventsNew = eventService.getAll(compilationDto.getEvents());
        Set<EventShortDto> eventShortDtos = eventService.getAllShortDto(compilationDto.getEvents());
        compilationMapper.updateCompilationFromDto(compilationDto, compilation, eventsNew);
        return compilationMapper.compilationToDto(compilation, eventShortDtos);
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        Compilation compilation = getOrThrow(compId);
        compilationRepository.delete(compilation);
    }

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        Pagination page = new Pagination(from, size);
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, page);
        List<CompilationDto> result = new ArrayList<>();
        for (Compilation compilation : compilations) {
            Set<Long> eventsIds = compilation.getEvents().stream()
                    .map(Event::getId)
                    .collect(Collectors.toSet());
            Set<EventShortDto> eventShortDtos = eventService.getAllShortDto(eventsIds);
            result.add(compilationMapper.compilationToDto(compilation, eventShortDtos));
        }
        return result;
    }

    @Override
    public CompilationDto getCompilation(Long compId) {
        Compilation compilation = getOrThrow(compId);
        Set<Long> eventsIds = compilation.getEvents().stream()
                .map(Event::getId)
                .collect(Collectors.toSet());
        Set<EventShortDto> eventShortDtos = eventService.getAllShortDto(eventsIds);

        return compilationMapper.compilationToDto(compilation, eventShortDtos);
    }

    private Compilation getOrThrow(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Сборник не найден"));
    }
}
