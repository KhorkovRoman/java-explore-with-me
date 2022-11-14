package ru.practicum.explorewithme.compilations.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.categories.dto.CategoryDto;
import ru.practicum.explorewithme.categories.mapper.CategoryMapper;
import ru.practicum.explorewithme.compilations.dto.NewCompilationDto;
import ru.practicum.explorewithme.compilations.service.CompilationService;

@Slf4j
@RestController
@RequestMapping(path = "/admin/compilations")
public class CompilationControllerAdmin {

    private final CompilationService compilationService;

    @Autowired
    public CompilationControllerAdmin(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

//    @PostMapping
//    public CategoryDto createCompilation(@RequestBody NewCompilationDto newCompilationDto) {
//        log.info("Has received request to endpoint POST/admin/compilations");
//        return CategoryMapper.toCategoryDto(compilationService.createCompilation(newCompilationDto));
//    }

}
