package ru.practicum.mainsvc.location.mapper;

import org.mapstruct.Mapper;
import ru.practicum.mainsvc.location.dto.LocationDto;
import ru.practicum.mainsvc.location.model.Location;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    Location locationFromDto(LocationDto locationDto);
}
