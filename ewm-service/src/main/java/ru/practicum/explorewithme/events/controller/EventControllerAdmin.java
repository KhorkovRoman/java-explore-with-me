package ru.practicum.explorewithme.events.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.common.ValidationPageParam;
import ru.practicum.explorewithme.events.dto.AdminUpdateEventRequest;
import ru.practicum.explorewithme.events.dto.EventFullDto;
import ru.practicum.explorewithme.events.mapper.EventMapper;
import ru.practicum.explorewithme.events.service.EventService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/events")
public class EventControllerAdmin {
    private final EventService eventService;

    @Autowired
    public EventControllerAdmin(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping()
    public Collection<EventFullDto> getAllEventsByAdmin(@RequestParam(required = false) List<Long> users,
                                                        @RequestParam(required = false) List<String> states,
                                                        @RequestParam(required = false) List<Long> categories,
                                                        @RequestParam(defaultValue = "") String rangeStart,
                                                        @RequestParam(defaultValue = "") String rangeEnd,
                                                        @RequestParam(defaultValue = "0") Integer from,
                                                        @RequestParam(defaultValue = "10") Integer size) {
        validatePage(from, size);
        log.info("Has received request to endpoint GET/admin/events?users={}states{}categories{}" +
                "rangeStart{}rangeEnd{}from={}size={}", users, states, categories, rangeStart, rangeEnd, from, size);
        final PageRequest pageRequest = findPageRequest(from, size);
        return eventService.getAllEventsByAdmin(users, states, categories, rangeStart, rangeEnd, pageRequest);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable Long eventId) {
        log.info("Has received request to endpoint PATCH/admin/events/{}/publish", eventId);
        return EventMapper.toEventFullDto(eventService.publishEvent(eventId));
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable Long eventId) {
        log.info("Has received request to endpoint PATCH/admin/events/{}/reject", eventId);
        return EventMapper.toEventFullDto(eventService.rejectEvent(eventId));
    }

    @PutMapping("/{eventId}")
    public EventFullDto editEventByAdmin(@PathVariable Long eventId,
                                    @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        log.info("Has received request to endpoint PUT/admin/events/{}", eventId);
        return EventMapper.toEventFullDto(eventService.editEventByAdmin(eventId, adminUpdateEventRequest));
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
