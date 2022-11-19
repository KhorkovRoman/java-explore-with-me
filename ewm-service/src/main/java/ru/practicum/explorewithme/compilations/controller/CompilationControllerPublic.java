package ru.practicum.explorewithme.compilations.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.common.ValidationPageParam;
import ru.practicum.explorewithme.compilations.dto.CompilationDto;
import ru.practicum.explorewithme.compilations.mapper.CompilationMapper;
import ru.practicum.explorewithme.compilations.service.CompilationService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/compilations")
public class CompilationControllerPublic {

    private final CompilationService compilationService;

    @Autowired
    public CompilationControllerPublic(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        log.info("Has received request to endpoint GET/compilations/{}", compId);
        return CompilationMapper.toCompilationDto(compilationService.getCompilationById(compId));
    }

    @GetMapping
    public Collection<CompilationDto> getAllCompilations(@RequestParam(defaultValue = "false") Boolean pinned,
                                                         @RequestParam(defaultValue = "0") Integer from,
                                                         @RequestParam(defaultValue = "10") Integer size) {
        validatePage(from, size);
        log.info("Has received request to endpoint GET/compilations?pinned={}from={}size={}", pinned, from, size);
        final PageRequest pageRequest = findPageRequest(from, size);
        return CompilationMapper.toCompilationDtoCollection(compilationService.getAllCompilations(pinned, pageRequest));
    }

    public PageRequest findPageRequest(Integer from, Integer size) {
        int page = from / size;
        return PageRequest.of(page, size);
    }

    private void validatePage(Integer from, Integer size) {
        ValidationPageParam validationPageParam = new ValidationPageParam(from, size);
        validationPageParam.validatePageParam();
    }
}
