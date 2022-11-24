package ru.practicum.explorewithme.dtos.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.common.Create;
import ru.practicum.explorewithme.models.request.RequestStatus;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipantRequestDto {
    private Long id;
    @NotNull(groups = {Create.class})
    private Long event;
    @NotNull(groups = {Create.class})
    private Long requester;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    @NotNull(groups = {Create.class})
    private RequestStatus status;
}
