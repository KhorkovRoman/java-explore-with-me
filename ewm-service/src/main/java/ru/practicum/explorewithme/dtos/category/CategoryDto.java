package ru.practicum.explorewithme.dtos.category;

import lombok.*;
import ru.practicum.explorewithme.common.Create;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Long id;
    @NotBlank(groups = {Create.class})
    private String name;
}