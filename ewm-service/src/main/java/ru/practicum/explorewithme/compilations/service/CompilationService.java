package ru.practicum.explorewithme.compilations.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.explorewithme.compilations.dto.CompilationDto;
import ru.practicum.explorewithme.compilations.dto.NewCompilationDto;
import ru.practicum.explorewithme.compilations.model.Compilation;

import java.util.Collection;

public interface CompilationService {

    Compilation createCompilation(NewCompilationDto newCompilationDto);

    Compilation updateCompilation(CompilationDto compilationDto);

    Collection<Compilation> getAllCompilations(PageRequest pageRequest);

    Compilation getCompilationById(Long compId);

    void deleteCompilation(Long compId);

}
