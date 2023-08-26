package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HitDtoInput {
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
