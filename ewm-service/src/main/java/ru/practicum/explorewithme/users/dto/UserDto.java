package ru.practicum.explorewithme.users.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ru.practicum.explorewithme.common.Create;
import ru.practicum.explorewithme.common.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Long id;
    @NotBlank(groups = {Create.class})
    private String name;
    @Email(groups = {Update.class, Create.class})
    @NotNull(groups = {Create.class})
    private String email;
}