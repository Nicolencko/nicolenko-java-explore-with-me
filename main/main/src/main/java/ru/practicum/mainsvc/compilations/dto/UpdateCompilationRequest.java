package ru.practicum.mainsvc.compilations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.mainsvc.utils.Update;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
public class UpdateCompilationRequest {
    private List<Long> events;
    private Boolean pinned;
    @Size(min = 1,
            max = 50, groups = {Update.class})
    private String title;

}
