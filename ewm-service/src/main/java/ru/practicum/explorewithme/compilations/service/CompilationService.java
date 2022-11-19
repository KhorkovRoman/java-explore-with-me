package ru.practicum.explorewithme.compilations.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.explorewithme.compilations.dto.NewCompilationDto;
import ru.practicum.explorewithme.compilations.model.Compilation;

import java.util.Collection;

public interface CompilationService {

    Compilation createCompilation(NewCompilationDto newCompilationDto);

    Collection<Compilation> getAllCompilations(Boolean pined, PageRequest pageRequest);

    Compilation getCompilationById(Long compId);

    void addEventToCompilation(Long compId, Long eventId);

    void deleteEventFromCompilation(Long compId, Long eventId);

    void deleteCompilation(Long compId);

    void pinCompilation(Long compId);
    void delPinCompilation(Long compId);
}
