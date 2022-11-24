package ru.practicum.explorewithme.dtos.category;

import lombok.*;
import ru.practicum.explorewithme.common.Create;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {
    @NotBlank(groups = {Create.class})
    private String name;
}