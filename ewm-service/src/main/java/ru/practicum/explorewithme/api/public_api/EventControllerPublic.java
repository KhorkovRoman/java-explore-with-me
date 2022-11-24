package ru.practicum.explorewithme.api.public_api;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.common.ValidationPageParam;
import ru.practicum.explorewithme.dtos.event.EventFullDto;
import ru.practicum.explorewithme.dtos.event.EventShortDto;
import ru.practicum.explorewithme.mappers.EventMapper;
import ru.practicum.explorewithme.services.event.EventService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

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
    public EventFullDto getEventByIdPublic(@PathVariable Long id, HttpServletRequest request) {
        log.info("Has received request to endpoint GET/events/{}", id);
        return EventMapper.toEventFullDto(eventService.getEventByIdPublic(id, request));
    }

    @SneakyThrows
    @GetMapping()
    public Collection<EventShortDto> getAllEventsByPublic(@RequestParam(defaultValue = "") String text,
                                                          @RequestParam(required = false) List<Long> categories,
                                                          @RequestParam(required = false) Boolean paid,
                                                          @RequestParam(defaultValue = "") String rangeStart,
                                                          @RequestParam(defaultValue = "") String rangeEnd,
                                                          @RequestParam (defaultValue = "false") Boolean onlyAvailable,
                                                          @RequestParam(defaultValue = "") String sort,
                                                          @RequestParam(defaultValue = "0") Integer from,
                                                          @RequestParam(defaultValue = "10") Integer size,
                                                          HttpServletRequest request) {
        validatePage(from, size);
        log.info("Has received request to endpoint GET/events?text={}categories{}paid={}rangeStart={}rangEnd{}" +
                "onlyAvailable={}sort={}from={}size={}", text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);
        final PageRequest pageRequest = findPageRequest(from, size);
        return eventService.getAllEventsByPublic(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, pageRequest, request);
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
