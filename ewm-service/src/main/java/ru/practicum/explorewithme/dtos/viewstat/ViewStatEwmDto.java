package ru.practicum.explorewithme.dtos.viewstat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.common.Create;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViewStatEwmDto {
    @NotBlank(groups = {Create.class})
    private String app;
    @NotBlank(groups = {Create.class})
    private String uri;
    @NotBlank(groups = {Create.class})
    private Long hits;
}
