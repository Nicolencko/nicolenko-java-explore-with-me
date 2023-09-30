package ru.practicum.mainsvc.event.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.mainsvc.event.model.EventUserRating;

@Mapper(componentModel = "spring")
public interface EventRatingMapper {

    @Mapping(target = "eventId", source = "eventUserRating.event.id")
    @Mapping(target = "userId", source = "eventUserRating.user.id")
    @Mapping(target = "rating", source = "eventUserRating.rating")
    EventRatingDto eventRateToDto(EventUserRating eventUserRating);

}
