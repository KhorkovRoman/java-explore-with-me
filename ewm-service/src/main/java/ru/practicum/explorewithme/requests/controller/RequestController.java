package ru.practicum.explorewithme.requests.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.requests.dto.ParticipantRequestDto;
import ru.practicum.explorewithme.requests.mapper.RequestMapper;
import ru.practicum.explorewithme.requests.service.RequestService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class RequestController {

    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
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
