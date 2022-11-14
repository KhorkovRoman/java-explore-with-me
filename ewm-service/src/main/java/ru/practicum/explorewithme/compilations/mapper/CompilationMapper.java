package ru.practicum.explorewithme.compilations.mapper;

import lombok.Data;
import ru.practicum.explorewithme.compilations.dto.NewCompilationDto;
import ru.practicum.explorewithme.compilations.model.Compilation;

@Data
public class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.getPinned())
                //.event(newCompilationDto.getEvents())
                .build();
    }

}
