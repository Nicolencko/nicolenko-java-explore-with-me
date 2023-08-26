package ru.practicum.mainsvc.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.mainsvc.event.dto.EventShortDto;

import java.util.List;

@Data
@AllArgsConstructor
public class CompilationDto {
    private List<EventShortDto> events;
    private Long id;
    private Boolean pinned;
    private String title;
}
