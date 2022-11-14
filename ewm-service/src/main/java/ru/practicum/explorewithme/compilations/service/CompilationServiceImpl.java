package ru.practicum.explorewithme.compilations.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.compilations.dto.CompilationDto;
import ru.practicum.explorewithme.compilations.dto.NewCompilationDto;
import ru.practicum.explorewithme.compilations.model.Compilation;
import ru.practicum.explorewithme.compilations.repository.CompilationRepository;

import java.util.Collection;

@Slf4j
@Service
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;

    @Autowired
    public CompilationServiceImpl(CompilationRepository compilationRepository) {
        this.compilationRepository = compilationRepository;
    }

    @Override
    public Compilation createCompilation(NewCompilationDto newCompilationDto) {
        return null;
    }

    @Override
    public Compilation updateCompilation(CompilationDto compilationDto) {
        return null;
    }

    @Override
    public Collection<Compilation> getAllCompilations(PageRequest pageRequest) {
        return null;
    }

    @Override
    public Compilation getCompilationById(Long compId) {
        return null;
    }

    @Override
    public void deleteCompilation(Long compId) {

    }
}
