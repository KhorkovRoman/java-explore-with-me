package ru.practicum.explorewithme.events.mapper;

import lombok.Data;
import ru.practicum.explorewithme.categories.mapper.CategoryMapper;
import ru.practicum.explorewithme.events.dto.EventFullDto;
import ru.practicum.explorewithme.events.dto.EventShortDto;
import ru.practicum.explorewithme.events.dto.Location;
import ru.practicum.explorewithme.events.dto.NewEventDto;
import ru.practicum.explorewithme.events.model.Event;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class EventMapper {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static List<EventShortDto> toEventShortDtoCollection(Collection<Event> eventCollection) {
        return eventCollection.stream()
                .map(EventMapper::toEventShotDto)
                .collect(Collectors.toList());
    }

    public static EventShortDto toEventShotDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .eventDate(event.getEventDate().format(FORMATTER))
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .paid(event.getPaid())
                .build();
    }

    public static List<EventFullDto> toEventFullDtoCollection(Collection<Event> eventCollection) {
        return eventCollection.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    public static EventFullDto toEventFullDto(Event event) {
        Location location = new Location();
        location.setLat(event.getLat());
        location.setLon(event.getLon());

        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(FORMATTER))
                .location(location)
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
