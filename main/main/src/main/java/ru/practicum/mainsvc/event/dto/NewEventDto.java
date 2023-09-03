package ru.practicum.mainsvc.event.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.mainsvc.utils.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class NewEventDto {
    @NotBlank(groups = {Create.class})
    @Size(min = 20, max = 2000, groups = {Create.class})
    private String annotation;

    @NotNull(groups = {Create.class})
    private Long category;

    @NotBlank(groups = {Create.class})
    @Size(min = 20, max = 7000, groups = {Create.class})
    private String description;

    @EventDate2Hours(groups = {Create.class})
    private String eventDate;

    @NotNull(groups = {Create.class})
    private Location location;
    @NotNull(groups = {Create.class})
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    @NotBlank(groups = {Create.class})
    @Size(min = 3, max = 120, groups = {Create.class})
    private String title;

    @Data
    @AllArgsConstructor
    public static class Location {
        Double lat;
        Double lon;
    }
}
