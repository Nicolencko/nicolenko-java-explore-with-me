package ru.practicum.mainsvc.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.mainsvc.utils.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned;
    @NotNull(groups = {Create.class})
    @NotBlank(groups = {Create.class})
    @Size(min = 1,
            max = 50, groups = {Create.class})
    private String title;
}
