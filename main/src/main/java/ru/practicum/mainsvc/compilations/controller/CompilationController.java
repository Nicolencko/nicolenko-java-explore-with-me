package ru.practicum.mainsvc.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsvc.compilations.dto.CompilationDto;
import ru.practicum.mainsvc.compilations.service.CompilationService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class CompilationController {
    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        log.info("Request endpoint: 'GET /compilations' (получение подборок событий)");
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable Long compId) {
        log.info("Request endpoint: 'GET /compilations/{}' (получение события)", compId);
        return compilationService.getCompilation(compId);
    }
}
