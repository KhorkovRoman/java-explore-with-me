package ru.practicum.explorewithme.dtos.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.dtos.category.CategoryDto;
import ru.practicum.explorewithme.common.Create;
import ru.practicum.explorewithme.dtos.user.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventShortDto {
    private Long id;
    @NotBlank(groups = {Create.class})
    private String title;
    @NotBlank(groups = {Create.class})
    private String annotation;
    @NotNull(groups = {Create.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String eventDate;
    @NotNull(groups = {Create.class})
    private CategoryDto category;
    private Long confirmedRequests;
    @NotNull(groups = {Create.class})
    private UserShortDto initiator;
    @NotNull(groups = {Create.class})
    private Boolean paid;
    private Long views;
}
