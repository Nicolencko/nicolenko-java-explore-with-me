package ru.practicum.model;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.HitDtoInput;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class HitMapper {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSS]");


    public static Hit mapInputToHit(HitDtoInput input) {
        return new Hit(
                null,
                input.getApp(),
                input.getUri(),
                input.getIp(),
                parseLocalDateTime(input.getTimestamp())
        );
    }

    private static LocalDateTime parseLocalDateTime(CharSequence text) {
        if (text == null) {
            return null;
        }
        return HitMapper.formatter.parse(text, LocalDateTime::from);
    }

}
