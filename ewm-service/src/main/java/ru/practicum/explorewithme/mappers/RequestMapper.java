package ru.practicum.explorewithme.mappers;

import lombok.Data;
import ru.practicum.explorewithme.dtos.request.ParticipantRequestDto;
import ru.practicum.explorewithme.models.request.Request;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class RequestMapper {

    public static List<ParticipantRequestDto> toParticipantRequestDtoCollection(Collection<Request> requests) {
        return requests.stream()
                .map(RequestMapper::toParticipantRequestDto)
                .collect(Collectors.toList());
    }

    public static ParticipantRequestDto toParticipantRequestDto(Request request) {
       return ParticipantRequestDto.builder()
           .id(request.getId())
           .event(request.getEvent().getId())
           .requester(request.getRequester().getId())
           .created(request.getCreated())
           .status(request.getStatus())
           .build();
    }

}
