package ru.practicum.service;

import ru.practicum.dto.HitDtoInput;
import ru.practicum.ViewStats;

import java.util.List;

public interface HitService {
    void saveHit(HitDtoInput hit);

    List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique);
}
