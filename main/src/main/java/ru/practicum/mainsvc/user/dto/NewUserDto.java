package ru.practicum.mainsvc.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewUserDto {
    private String email;
    private String name;
}
