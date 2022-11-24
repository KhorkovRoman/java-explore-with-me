package ru.practicum.explorewithme.api.private_api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dtos.request.ParticipantRequestDto;
import ru.practicum.explorewithme.mappers.RequestMapper;
import ru.practicum.explorewithme.services.request.RequestService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class RequestControllerPrivate {

    private final RequestService requestService;

    @Autowired
    public RequestControllerPrivate(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping("/{userId}/requests")
    public ParticipantRequestDto createRequest(@PathVariable Long userId,
                                               @RequestParam(required = false) Long eventId) {
        log.info("Has received request to endpoint POST/users/{}/requests?eventId={}", userId, eventId);
        return RequestMapper.toParticipantRequestDto(requestService.createRequest(userId, eventId));
    }

    @PatchMapping("/{userId}/requests/{reqId}/cancel")
    public ParticipantRequestDto cancelRequest(@PathVariable(required = true) Long userId,
                                               @PathVariable(required = true) Long reqId) {
        log.info("Has received request to endpoint PATCH/users/{}/requests/{}/cancel", userId, reqId);
        return RequestMapper.toParticipantRequestDto(requestService.cancelRequest(userId, reqId));
    }

    @GetMapping("/{userId}/requests")
    public Collection<ParticipantRequestDto> getAllRequestsByUser(@PathVariable(required = true) Long userId) {
        log.info("Has received request to endpoint GET/users/{}/requests", userId);
        return RequestMapper.toParticipantRequestDtoCollection(requestService.getAllRequestsByUser(userId));
    }
}
