package ru.practicum.mainsvc.request.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.mainsvc.request.dto.ParticipationRequestDto;
import ru.practicum.mainsvc.request.model.Request;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RequestMapper {
    public ParticipationRequestDto mapToDto(Request request) {
        return new ParticipationRequestDto(
                request.getCreated(),
                request.getEventId(),
                request.getId(),
                request.getRequesterId(),
                request.getStatus()
        );
    }

    public Request mapDtoToNewRequest(Long userId, Long eventId) {
        return new Request(
                null,
                LocalDateTime.now(),
                eventId,
                userId,
                "PENDING"
        );
    }
}
