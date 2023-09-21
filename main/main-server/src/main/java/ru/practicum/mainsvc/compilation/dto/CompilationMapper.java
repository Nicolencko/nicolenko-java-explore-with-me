package ru.practicum.mainsvc.compilation.mapper;

import org.mapstruct.*;
import ru.practicum.mainsvc.compilation.dto.CompilationDto;
import ru.practicum.mainsvc.compilation.dto.NewCompilationDto;
import ru.practicum.mainsvc.compilation.dto.UpdateCompilationDto;
import ru.practicum.mainsvc.compilation.model.Compilation;
import ru.practicum.mainsvc.event.dto.EventShortDto;
import ru.practicum.mainsvc.event.model.Event;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface CompilationMapper {
    @Mapping(target = "events", source = "events")
    Compilation compilationFromNewDto(NewCompilationDto compilationDto, Set<Event> events);

    @Mapping(target = "events", source = "events")
    CompilationDto compilationToDto(Compilation compilation, Set<EventShortDto> events);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "events", source = "eventsNew")
    void updateCompilationFromDto(UpdateCompilationDto compilationDto,
                                  @MappingTarget Compilation compOld,
                                  Set<Event> eventsNew);
}
