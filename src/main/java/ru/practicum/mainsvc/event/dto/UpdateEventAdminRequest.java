package ru.practicum.mainsvc.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.Nullable;
import ru.practicum.mainsvc.utils.Update;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class UpdateEventAdminRequest {
    @Nullable
    @Size(min = 20,
            max = 2000, groups = {Update.class})
    private String annotation;
    private Long category;
    @Nullable
    @Size(min = 20,
            max = 7000, groups = {Update.class})
    private String description;
    @Nullable
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String stateAction;
    @Nullable
    @Size(min = 3,
            max = 120, groups = {Update.class})
    private String title;

    @Data
    @AllArgsConstructor
    public static class Location {
        Double lat;
        Double lon;
    }
}
