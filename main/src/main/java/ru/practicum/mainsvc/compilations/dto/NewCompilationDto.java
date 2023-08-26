package ru.practicum.mainsvc.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.mainsvc.utils.Create;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned;
    @NotNull(groups = {Create.class})
    private String title;
}
