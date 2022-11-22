package ru.practicum.explorewithme.mappers;

import lombok.Data;
import ru.practicum.explorewithme.dtos.compilation.CompilationDto;
import ru.practicum.explorewithme.dtos.compilation.NewCompilationDto;
import ru.practicum.explorewithme.models.compilation.Compilation;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CompilationMapper {

    public static List<CompilationDto> toCompilationDtoCollection(Collection<Compilation> compilations) {
        return compilations.stream()
                .map(CompilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    public static CompilationDto toCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(EventMapper.toEventShortDtoCollection(compilation.getEvents()))
                .build();
    }

    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.getPinned())
                .build();
    }
}
