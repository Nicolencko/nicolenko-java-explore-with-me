package ru.practicum.mainsvc.compilation.service;

import ru.practicum.mainsvc.compilation.dto.CompilationDto;
import ru.practicum.mainsvc.compilation.dto.NewCompilationDto;
import ru.practicum.mainsvc.compilation.dto.UpdateCompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto saveCompilation(NewCompilationDto compilationDto);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, UpdateCompilationDto compilationDto);

    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilation(Long compId);
}
