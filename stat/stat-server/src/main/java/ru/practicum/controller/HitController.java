package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.HitDtoInput;
import ru.practicum.ViewStats;
import ru.practicum.service.HitService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class HitController {
    private final HitService hitService;

    @PostMapping("/hit")
    public void saveHit(@RequestBody HitDtoInput hit) {
        hitService.saveHit(hit);
    }

    @GetMapping("/stats")
    public List<ViewStats> getHits(
            @RequestParam(name = "start") String start,
            @RequestParam(name = "end") String end,
            @RequestParam(required = false, name = "uris") List<String> uris,
            @RequestParam(required = false, defaultValue = "false", name = "unique") Boolean unique) {
        return hitService.getStats(start, end, uris, unique);
    }
}
