package ru.practicum.explorewithme.events.mapper;

import lombok.Data;
import ru.practicum.explorewithme.categories.mapper.CategoryMapper;
import ru.practicum.explorewithme.categories.service.CategoryService;
import ru.practicum.explorewithme.events.dto.EventFullDto;
import ru.practicum.explorewithme.events.dto.EventShortDto;
import ru.practicum.explorewithme.events.dto.NewEventDto;
import ru.practicum.explorewithme.events.model.Event;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class EventMapper {

    private static CategoryService categoryService;

    public static List<EventShortDto> toEventShortDtoCollection(Collection<Event> eventCollection ) {
        return eventCollection.stream()
                .map(EventMapper::toEventShotDto)
                .collect(Collectors.toList());
    }

    public static EventShortDto toEventShotDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .eventDate(event.getEventDate())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .location(event.getLocation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .participantLimit(event.getParticipantLimit())
                .paid(event.getPaid())
                .requestModeration(event.getRequestModeration())
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .publishedOn(event.getPublishedOn())
                .views(event.getViews())
                .initiator(event.getInitiator())
                .state(event.getState().toString())
                .build();
    }

    public static Event toEvent(NewEventDto newEventDto) {
        return Event.builder()
                .title(newEventDto.getTitle())
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .build();
    }

}
