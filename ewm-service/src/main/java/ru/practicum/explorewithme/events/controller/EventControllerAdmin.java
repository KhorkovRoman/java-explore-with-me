package ru.practicum.explorewithme.events.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.events.dto.EventFullDto;
import ru.practicum.explorewithme.events.mapper.EventMapper;
import ru.practicum.explorewithme.events.service.EventService;

@Slf4j
@RestController
@RequestMapping(path = "/admin/events")
public class EventControllerAdmin {
    private final EventService eventService;
    @Autowired
    public EventControllerAdmin(EventService eventService) {
        this.eventService = eventService;
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
}
