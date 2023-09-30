package ru.practicum.mainsvc.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRatingDto {
    private Long eventId;
    private Long userId;
    private Long rating;
}