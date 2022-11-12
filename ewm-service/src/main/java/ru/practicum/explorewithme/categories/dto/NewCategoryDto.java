package ru.practicum.explorewithme.categories.dto;

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