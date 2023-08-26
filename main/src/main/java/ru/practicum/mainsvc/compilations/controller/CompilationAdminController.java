package ru.practicum.mainsvc.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.mainsvc.compilations.dto.CompilationDto;
import ru.practicum.mainsvc.compilations.dto.NewCompilationDto;
import ru.practicum.mainsvc.compilations.dto.UpdateCompilationRequest;
import ru.practicum.mainsvc.compilations.model.EventsCompilations;
import ru.practicum.mainsvc.compilations.service.CompilationService;
import ru.practicum.mainsvc.compilations.service.EventsCompilationsService;
import ru.practicum.mainsvc.utils.Create;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")

public class CompilationAdminController {
    private final CompilationService compilationService;
    private final EventsCompilationsService eventsCompilationsService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CompilationDto saveCompilation(
            @Validated({Create.class}) @RequestBody NewCompilationDto compilationDto
    ) {
        log.info("Request endpoint: 'POST /admin/compilations' (Добавление подборки)");
        return compilationService.saveCompilation(compilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Request endpoint: 'DELETE /admin/compilations/{}' (Удаление подборки)", compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId,
                                            @RequestBody UpdateCompilationRequest request) {
        log.info("Request endpoint: 'PATCH /admin/compilations/{}' (Изменение подборки)", compId);
        return compilationService.updateCompilation(compId, request);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(
            @PathVariable Long compId,
            @PathVariable Long eventId
    ) {
        log.info("Request endpoint: 'GET /admin/compilations/{}/events/{}' " +
                "(Удаление события из подборки)", compId, eventId);
        compilationService.deleteEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventToCompilation(
            @PathVariable Long compId,
            @PathVariable Long eventId
    ) {
        log.info("Request endpoint: 'GET /admin/compilations/{}/events/{}' " +
                "(Добавление события в подборку)", compId, eventId);
        eventsCompilationsService.saveEventCompilation(new EventsCompilations(compId, eventId));
    }

    @DeleteMapping("/{compId}/pin")
    public void unpinCompilation(@PathVariable Long compId) {
        log.info("Request endpoint: 'GET /admin/compilations/{}/pin' " +
                "(Открепление подборки)", compId);
        compilationService.unpinCompilation(compId);
    }

    @PatchMapping("/{compId}/pin")
    public void pinCompilation(@PathVariable Long compId) {
        log.info("Request endpoint: 'GET /admin/compilations/{}/pin' " +
                "(Закрепление подборки)", compId);
        compilationService.pinCompilation(compId);
    }
}
