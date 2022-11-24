package ru.practicum.explorewithme.api.admin_api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dtos.compilation.CompilationDto;
import ru.practicum.explorewithme.dtos.compilation.NewCompilationDto;
import ru.practicum.explorewithme.mappers.CompilationMapper;
import ru.practicum.explorewithme.services.compilation.CompilationService;

@Slf4j
@RestController
@RequestMapping(path = "/admin/compilations")
public class CompilationControllerAdmin {

    private final CompilationService compilationService;

    @Autowired
    public CompilationControllerAdmin(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping
    public CompilationDto createCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        log.info("Has received request to endpoint POST/admin/compilations");
        return CompilationMapper.toCompilationDto(compilationService.createCompilation(newCompilationDto));
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Has received request to endpoint DELETE/admin/compilation/{}", compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable Long compId,
                                      @PathVariable Long eventId) {
        log.info("Has received request to endpoint PATCH/admin/compilations/{}/events/{}", compId, eventId);
        compilationService.addEventToCompilation(compId, eventId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable Long compId,
                                           @PathVariable Long eventId) {
        log.info("Has received request to endpoint DELETE/admin/compilations/{}/events/{}", compId, eventId);
        compilationService.deleteEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/pin")
    public void pinCompilation(@PathVariable Long compId) {
        log.info("Has received request to endpoint PATCH/admin/compilations/{}/pin", compId);
        compilationService.pinCompilation(compId);
    }

    @DeleteMapping("/{compId}/pin")
    public void delPinCompilation(@PathVariable Long compId) {
        log.info("Has received request to endpoint DELETE/admin/compilations/{}/pin", compId);
        compilationService.delPinCompilation(compId);
    }
}
