package ru.practicum.explorewithme.dtos.location;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.common.Create;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Location {
    @NotNull(groups = {Create.class})
    private Float lat;
    @NotNull(groups = {Create.class})
    private Float lon;
}
