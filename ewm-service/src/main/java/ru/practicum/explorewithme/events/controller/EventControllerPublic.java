package ru.practicum.explorewithme.events.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.events.dto.EventFullDto;
import ru.practicum.explorewithme.events.mapper.EventMapper;
import ru.practicum.explorewithme.events.service.EventService;

@Slf4j
@RestController
@RequestMapping(path = "/events")
public class EventControllerPublic {

    private final EventService eventService;

    @Autowired
    public EventControllerPublic(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/{id}")
    public EventFullDto getEventByIdPublic(@PathVariable Long id) {
        log.info("Has received request to endpoint GET/events/{}", id);
        return EventMapper.toEventFullDto(eventService.getEventByIdPublic(id));
    }

}
