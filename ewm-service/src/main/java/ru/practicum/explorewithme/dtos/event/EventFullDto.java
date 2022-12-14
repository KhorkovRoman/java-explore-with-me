package ru.practicum.explorewithme.dtos.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.dtos.category.CategoryDto;
import ru.practicum.explorewithme.common.Create;
import ru.practicum.explorewithme.dtos.location.Location;
import ru.practicum.explorewithme.models.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventFullDto {
    private Long id;
    @NotBlank(groups = {Create.class})
    private String title;
    @NotBlank(groups = {Create.class})
    private String annotation;
    private String description;
    @NotNull(groups = {Create.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String eventDate;
    @NotNull(groups = {Create.class})
    private Location location;
    @NotNull(groups = {Create.class})
    private CategoryDto category;
    private Long participantLimit;
    @NotNull(groups = {Create.class})
    private Boolean paid;
    private Boolean requestModeration;
    private Long confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private Long views;
    @NotNull(groups = {Create.class})
    private User initiator;
    private String state;
}
