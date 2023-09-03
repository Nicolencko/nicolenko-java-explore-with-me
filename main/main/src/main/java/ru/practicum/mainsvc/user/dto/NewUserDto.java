package ru.practicum.mainsvc.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class NewUserDto {
    @NotBlank(message = "Must not be blank.")
    @Email(message = "The entered email is incorrect.")
    @Size(min = 6,
            max = 254,
            message = "Email field size must be between 6 and 254 characters.")
    private String email;

    @NotBlank(message = "Must not be blank.")
    @Size(min = 2,
            max = 250,
            message = "Name field size must be between 2 and 250 characters.")
    private String name;
}
