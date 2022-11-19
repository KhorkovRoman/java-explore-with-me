package ru.practicum.explorewithme.events.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.common.ValidationPageParam;
import ru.practicum.explorewithme.events.dto.EventFullDto;
import ru.practicum.explorewithme.events.dto.EventShortDto;
import ru.practicum.explorewithme.events.dto.NewEventDto;
import ru.practicum.explorewithme.events.dto.UpdateEventRequest;
import ru.practicum.explorewithme.events.mapper.EventMapper;
import ru.practicum.explorewithme.events.service.EventService;
import ru.practicum.explorewithme.requests.dto.ParticipantRequestDto;
import ru.practicum.explorewithme.requests.mapper.RequestMapper;
import ru.practicum.explorewithme.requests.service.RequestService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/users")
public class EventControllerPrivate {

    private final EventService eventService;
    private final RequestService requestService;

    @Autowired
    public EventControllerPrivate(EventService eventService, RequestService requestService) {
        this.eventService = eventService;
        this.requestService = requestService;
    }

    @PostMapping("/{userId}/events")
    public EventFullDto createEvent(@PathVariable Long userId,
                                    @RequestBody NewEventDto newEventDto) {
        log.info("Has received request to endpoint POST/users/{}/events", userId);
        return EventMapper.toEventFullDto(eventService.createEvent(userId, newEventDto));
    }

    @GetMapping("/{userId}/events")
    public Collection<EventShortDto> getAllEventsByUser(@PathVariable Long userId,
                                                        @RequestParam(defaultValue = "0") Integer from,
                                                        @RequestParam(defaultValue = "10") Integer size) {
        validatePage(from, size);
        log.info("Has received request to endpoint GET/users/{}/events?from={}size={}", userId, from, size);
        final PageRequest pageRequest = findPageRequest(from, size);
        return eventService.getAllEventsByUser(userId, pageRequest);
    }

    @PatchMapping("/{userId}/events")
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @RequestBody UpdateEventRequest updateEventRequest) {
        log.info("Has received request to endpoint PATCH/users/{}/events", userId);
        return EventMapper.toEventFullDto(eventService.updateEvent(userId, updateEventRequest));
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto cancelEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId) {
        log.info("Has received request to endpoint PATCH/users/{}/events/{}", userId, eventId);
        return EventMapper.toEventFullDto(eventService.cancelEvent(userId, eventId));
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventById(@PathVariable Long userId,
                                     @PathVariable Long eventId) {
        log.info("Has received request to endpoint GET/users/{}/events/{}", userId, eventId);
        return EventMapper.toEventFullDto(eventService.getEventById(eventId));
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public Collection<ParticipantRequestDto> getAllRequestsByEvent(@PathVariable(required = true) Long userId,
                                                                   @PathVariable(required = true) Long eventId) {
        log.info("Has received request to endpoint GET/users/{}/events/{}/requests", userId, eventId);
        return RequestMapper.toParticipantRequestDtoCollection(requestService.getAllRequestsByEvent(userId, eventId));
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ParticipantRequestDto confirmRequest(@PathVariable(required = true) Long userId,
                                                @PathVariable(required = true) Long eventId,
                                                @PathVariable(required = true) Long reqId) {
        log.info("Has received request to endpoint PATCH/users/{}/events/{}/requests/{}/confirm",
                userId, eventId, reqId);
        return RequestMapper.toParticipantRequestDto(requestService.confirmRequest(userId, eventId, reqId));
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ParticipantRequestDto rejectRequest(@PathVariable(required = true) Long userId,
                                               @PathVariable(required = true) Long eventId,
                                               @PathVariable(required = true) Long reqId) {
        log.info("Has received request to endpoint PATCH/users/{}/events/{}/requests/{}/reject",
                userId, eventId, reqId);
        return RequestMapper.toParticipantRequestDto(requestService.rejectRequest(userId, eventId, reqId));
    }

    public PageRequest findPageRequest(Integer from, Integer size) {
        int page = from / size;
        return PageRequest.of(page, size);
    }

    private void validatePage(Integer from, Integer size) {
        ValidationPageParam validationPageParam = new ValidationPageParam(from, size);
        validationPageParam.validatePageParam();
    }
}
