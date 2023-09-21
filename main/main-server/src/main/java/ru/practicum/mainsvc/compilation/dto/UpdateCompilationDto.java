package ru.practicum.mainsvc.compilation.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
public class UpdateCompilationDto {

    private Set<Long> events;

    private Boolean pinned;

    @Size(min = 1,
            max = 50)
    private String title;
}