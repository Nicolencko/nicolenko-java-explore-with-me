package ru.practicum.mainsvc.category.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.mainsvc.utils.Create;
import ru.practicum.mainsvc.utils.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {
    @NotBlank(groups = {Create.class, Update.class})
    @NotNull(groups = {Create.class, Update.class})
    @Size(min = 1,
            max = 50, groups = {Create.class, Update.class})
    String name;
}
