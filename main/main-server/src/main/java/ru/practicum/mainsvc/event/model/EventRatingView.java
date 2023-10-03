package ru.practicum.mainsvc.event.model;

import java.util.Optional;

public interface EventRatingView {

    Optional<Long> getEventId();

    Optional<String> getTitle();

    Long getRating();

}
