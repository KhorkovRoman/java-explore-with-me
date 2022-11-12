package ru.practicum.explorewithme.events.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.common.Create;
import ru.practicum.explorewithme.events.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewEventDto {
    @NotBlank(groups = {Create.class})
    private String title;
    @NotBlank(groups = {Create.class})
    private String annotation;
    @NotBlank(groups = {Create.class})
    private String description;
    @NotNull(groups = {Create.class})
    private LocalDateTime eventDate;
    @NotNull(groups = {Create.class})
    private Location location;
    @NotNull(groups = {Create.class})
    private Long category;

}
