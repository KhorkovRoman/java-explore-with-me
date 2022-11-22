package ru.practicum.explorewithme.dtos.compilation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.common.Create;
import ru.practicum.explorewithme.common.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    @NotBlank(groups = {Create.class})
    private String title;
    @NotNull(groups = {Create.class})
    private Boolean pinned;
    @NotNull(groups = {Create.class})
    private Collection<Long> events;
}
