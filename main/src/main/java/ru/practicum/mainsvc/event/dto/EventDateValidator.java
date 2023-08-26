package ru.practicum.mainsvc.event.dto;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventDateValidator implements ConstraintValidator<EventDate2Hours, String> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss[.SSS]");


    @Override
    public void initialize(EventDate2Hours constraint) {
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext context) {

        if (date == null) {
            return true;
        }

        if (parseLocalDateTime(date, formatter).isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Недопустимая дата");
        }
        return true;
    }

    private static LocalDateTime parseLocalDateTime(CharSequence text, DateTimeFormatter formatter) {
        if (text == null) {
            return null;
        }
        return formatter.parse(text, LocalDateTime::from);
    }
}
