package ru.practicum.mainsvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"ru.practicum.mainsvc", "ru.practicum"})
public class MainSvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainSvcApplication.class, args);
    }
}
