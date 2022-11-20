package ru.practicum.explorewithme.events.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.categories.model.Category;
import ru.practicum.explorewithme.categories.repository.CategoryRepository;
import ru.practicum.explorewithme.exeption.BadRequestException;
import ru.practicum.explorewithme.exeption.NotFoundException;
import ru.practicum.explorewithme.events.client.EventClient;
import ru.practicum.explorewithme.events.dto.*;
import ru.practicum.explorewithme.events.mapper.EventMapper;
import ru.practicum.explorewithme.events.model.Event;
import ru.practicum.explorewithme.events.model.EventStatus;
import ru.practicum.explorewithme.events.repository.EventRepository;
import ru.practicum.explorewithme.requests.model.RequestStatus;
import ru.practicum.explorewithme.requests.repository.RequestRepository;
import ru.practicum.explorewithme.users.model.User;
import ru.practicum.explorewithme.users.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventServiceImpl implements EventService {

    public static final String NAME_OF_APP = "ewm-main-service";

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final String EVENT_DATE = "EVENT_DATE";

    private static final String VIEWS = "VIEWS";

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private final RequestRepository requestRepository;
    private final EventClient eventClient;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository, CategoryRepository categoryRepository,
                            UserRepository userRepository, RequestRepository requestRepository,
                            EventClient eventClient) {
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.eventClient = eventClient;
    }

    @Override
    public Event createEvent(Long userId, NewEventDto newEventDto) {
        Event event = EventMapper.toEvent(newEventDto);
        validateEvent(event);
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException(
                        "In DB has not found category with id " + newEventDto.getCategory()));
        event.setCategory(category);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        "In DB has not found user id " + userId));
        event.setInitiator(user);

        event.setLon(newEventDto.getLocation().getLon());
        event.setLat(newEventDto.getLocation().getLat());

        event.setState(EventStatus.PENDING);

        Event eventDB = eventRepository.save(event);
        log.info("Event id " + eventDB.getId() + " has successfully created.");
        return eventDB;
    }

    private Event updateParamEventByUser(Event event, UpdateEventRequest updateEventRequest) {
        validateEvent(event);
        if (updateEventRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException(
                            "In DB has not found category id " + updateEventRequest.getCategory()));
            event.setCategory(category);
        }
        if (updateEventRequest.getDescription() != null) {
            event.setDescription(updateEventRequest.getDescription());
        }
        if (updateEventRequest.getEventDate() != null) {
            event.setEventDate(updateEventRequest.getEventDate());
        }
        if (updateEventRequest.getPaid() != null) {
            event.setPaid(updateEventRequest.getPaid());
        }
        if (updateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }
        if (updateEventRequest.getTitle() != null) {
            event.setTitle(updateEventRequest.getTitle());
        }
        return event;
    }

    @Override
    public Event updateEvent(Long userId, UpdateEventRequest updateEventRequest) {
        Long eventId = updateEventRequest.getEventId();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Has not found event with id " + eventId));
        Event eventDB = eventRepository.save(updateParamEventByUser(event, updateEventRequest));
        log.info("Event id " + eventDB.getId() + " has successfully updated.");
        return eventDB;
    }

    private Event updateParamEventByAdmin(Event event, AdminUpdateEventRequest adminUpdateEventRequest) {
        if (adminUpdateEventRequest.getAnnotation() != null) {
            event.setAnnotation(adminUpdateEventRequest.getAnnotation());
        }
        if (adminUpdateEventRequest.getCategory() != null) {
            Category category = categoryRepository.findById(adminUpdateEventRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException(
                            "In DB has not found category id " + adminUpdateEventRequest.getCategory()));
            event.setCategory(category);
        }
        if (adminUpdateEventRequest.getDescription() != null) {
            event.setDescription(adminUpdateEventRequest.getDescription());
        }
        if (adminUpdateEventRequest.getEventDate() != null) {
            event.setEventDate(adminUpdateEventRequest.getEventDate());
        }
        if (adminUpdateEventRequest.getLocation() != null) {
            event.setLat(adminUpdateEventRequest.getLocation().getLat());
            event.setLon(adminUpdateEventRequest.getLocation().getLon());
        }
        if (adminUpdateEventRequest.getPaid() != null) {
            event.setPaid(adminUpdateEventRequest.getPaid());
        }
        if (adminUpdateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(adminUpdateEventRequest.getParticipantLimit());
        }
        if (adminUpdateEventRequest.getRequestModeration() != null) {
            event.setRequestModeration(adminUpdateEventRequest.getRequestModeration());
        }
        if (adminUpdateEventRequest.getTitle() != null) {
            event.setTitle(adminUpdateEventRequest.getTitle());
        }
        return event;
    }

    @Override
    public Event editEventByAdmin(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Has not found event with id " + eventId));
        Event eventDB = eventRepository.save(updateParamEventByAdmin(event, adminUpdateEventRequest));
        log.info("Event id " + eventDB.getId() + " has successfully updated.");
        return eventDB;
    }

    @Override
    public Event cancelEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Has not found event with id " + eventId));
        event.setState(EventStatus.CANCELED);
        Event eventDB = eventRepository.save(event);
        log.info("Event id " + eventDB.getId() + " has successfully canceled.");
        return eventDB;
    }

    @Override
    public Event publishEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Has not found event id " + eventId));
        event.setState(EventStatus.PUBLISHED);
        Event eventDB = eventRepository.save(event);
        log.info("Event id " + eventDB.getId() + " has successfully published.");
        return eventDB;
    }

    @Override
    public Event rejectEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Has not found event id " + eventId));
        event.setState(EventStatus.CANCELED);
        Event eventDB = eventRepository.save(event);
        log.info("Event id " + eventDB.getId() + " has successfully rejected.");
        return eventDB;
    }

    @Override
    public Collection<EventShortDto> getAllEventsByUser(Long userId, PageRequest pageRequest) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        "In DB has not found user id " + userId));
        Collection<Event> eventCollection = eventRepository.getAllEventsByUser(userId, pageRequest).stream()
                .collect(Collectors.toList());

        return EventMapper.toEventShortDtoCollection(eventCollection);
    }

    @Override
    public Collection<EventFullDto> getAllEventsByAdmin(List<Long> users, List<String> states,
                                                        List<Long> categories,
                                                        String rangeStart, String rangeEnd,
                                                        PageRequest pageRequest) {
        LocalDateTime rangeStartFormatted;
        if (rangeStart.equals("")) {
            rangeStartFormatted = LocalDateTime.now();
        } else {
            rangeStartFormatted = LocalDateTime.parse(rangeStart, FORMATTER);
        }
        LocalDateTime rangeEndFormatted;
        if (rangeEnd.equals("")) {
            rangeEndFormatted = LocalDateTime.now();
        } else {
            rangeEndFormatted = LocalDateTime.parse(rangeEnd, FORMATTER);
        }

        List<Category> categoryEntities = new ArrayList<>();
        for (Long catId: categories) {
            Category category = categoryRepository.findById(catId)
                    .orElseThrow(() -> new NotFoundException(
                            "In DB has not found category with id " + catId));
            categoryEntities.add(category);
        }

        List<User> userEntities = new ArrayList<>();
        for (Long userId: users) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException(
                            "In DB has not found category with id " + userId));
            userEntities.add(user);
        }

        List<EventStatus> statesEnum = new ArrayList<>();
        if (states != null) {
            for (String state: states) {
                EventStatus eventStatus = EventStatus.findState(state);
                statesEnum.add(eventStatus);
            }
        } else {
            statesEnum.addAll(Arrays.asList(EventStatus.values()));
        }

        Page<Event> events = eventRepository.getAllEventsByAdmin(userEntities, statesEnum,
                categoryEntities, rangeStartFormatted, rangeEndFormatted,
                pageRequest);

        return EventMapper.toEventFullDtoCollection(events.getContent());
    }

    @Override
    public Collection<EventShortDto> getAllEventsByPublic(String text, List<Long> categories, Boolean paid,
                                                          String rangeStart, String rangeEnd,
                                                          Boolean onlyAvailable, String sort,
                                                          PageRequest pageRequest, HttpServletRequest request) {

        LocalDateTime rangeStartFormatted;
        if (rangeStart.equals("")) {
            rangeStartFormatted = LocalDateTime.now();
        } else {
            rangeStartFormatted = LocalDateTime.parse(rangeStart, FORMATTER);
        }
        LocalDateTime rangeEndFormatted;
        if (rangeEnd.equals("")) {
            rangeEndFormatted = LocalDateTime.now().plusYears(50);
        } else {
            rangeEndFormatted = LocalDateTime.parse(rangeEnd, FORMATTER);
        }

        List<Category> categoryEntities = new ArrayList<>();
        for (Long catId : categories) {
            Category category = categoryRepository.findById(catId)
                    .orElseThrow(() -> new NotFoundException(
                            "In DB has not found category with id " + catId));
            categoryEntities.add(category);
        }

        Page<Event> events = null;
        if (text.equals("")) {
            if (onlyAvailable.equals(true)) {
                if (sort.equals(EVENT_DATE) || sort.equals("")) {
                    events = eventRepository.getAllEventsPublicByEventDateAvailableAllText(text, categoryEntities, paid,
                            rangeStartFormatted, rangeEndFormatted, pageRequest);
                }
                if (sort.equals(VIEWS)) {
                    events = eventRepository.getAllEventsPublicByViewsAvailableAllText(text, categoryEntities, paid,
                            rangeStartFormatted, rangeEndFormatted, pageRequest);
                }
            }
        } else {
            if (onlyAvailable.equals(true)) {
                if (sort.equals(EVENT_DATE) || sort.equals("")) {
                    events = eventRepository.getAllEventsPublicByEventDateAvailable(text, categoryEntities, paid,
                            rangeStartFormatted, rangeEndFormatted, pageRequest);
                }
                if (sort.equals(VIEWS)) {
                    events = eventRepository.getAllEventsPublicByViewsAvailable(text, categoryEntities, paid,
                            rangeStartFormatted, rangeEndFormatted, pageRequest);
                }
            }
        }

        if (text.equals("")) {
            if (sort.equals(EVENT_DATE) || sort.equals("")) {
                events = eventRepository.getAllEventsPublicByEventDateAllText(text, categoryEntities, paid,
                        rangeStartFormatted, rangeEndFormatted, pageRequest);
            }
            if (sort.equals(VIEWS)) {
                events = eventRepository.getAllEventsPublicByViewsAllText(text, categoryEntities, paid,
                        rangeStartFormatted, rangeEndFormatted, pageRequest);
            }
        } else {
            if (sort.equals(EVENT_DATE) || sort.equals("")) {
                events = eventRepository.getAllEventsPublicByEventDate(text, categoryEntities, paid,
                        rangeStartFormatted, rangeEndFormatted, pageRequest);
            }
            if (sort.equals(VIEWS)) {
                events = eventRepository.getAllEventsPublicByViews(text, categoryEntities, paid,
                        rangeStartFormatted, rangeEndFormatted, pageRequest);
            }
        }

        List<EventShortDto> eventList = EventMapper.toEventShortDtoCollection(events.getContent());

        List<String> urisCount = new ArrayList<>();
        for (EventShortDto event : eventList) {
            String uri = "/events/" + event.getId().toString();
            urisCount.add(uri);
        }

        StringBuilder urisBuilder = new StringBuilder();
        for (int i = 0; i < urisCount.size(); i++) {
            if (i < (urisCount.size() - 1)) {
                urisBuilder.append(urisCount.get(i)).append("&");
            } else {
                urisBuilder.append(urisCount.get(i));
            }
        }

        String uris = urisBuilder.toString();

        ArrayList<LinkedHashMap> listFromObject = findViews(uris);

        if (!listFromObject.isEmpty()) {
            for (LinkedHashMap linkedHashMap : listFromObject) {
                String[] split = linkedHashMap.get("uri").toString().split(",");
                for (EventShortDto event : eventList) {
                    if (event.getId().equals(Long.parseLong(split[2]))) {
                        Long views = Long.parseLong(String.valueOf(linkedHashMap.get("hits")));
                        event.setViews(views);
                        log.info("For event id " + event.getId() + " has set views = " + event.getViews());
                    }
                }
            }
        } else {
            for (EventShortDto event : eventList) {
                event.setViews(0L);
                log.info("For event id " + event.getId() + " has set views = " + event.getViews());
            }
        }

        for (EventShortDto event : eventList) {
            event.setConfirmedRequests(confirmedRequests(event.getId()));
            log.info("For event id " + event.getId() + " has set confirmed requests = " + event.getConfirmedRequests());
        }

        eventClient.createEndpointHit(makeEndpointHitEwmDto(request));

        return eventList;
    }

    @Override
    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Has not found event id " + eventId));
    }

    @Override
    public Event getEventByIdPublic(Long id, HttpServletRequest request) {
        eventClient.createEndpointHit(makeEndpointHitEwmDto(request));
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Has not found event id " + id));

        String uris = request.getRequestURI();
        ArrayList<LinkedHashMap> listFromObject = findViews(uris);
        LinkedHashMap linkedHashMap = listFromObject.get(0);
        Long views = Long.parseLong(String.valueOf(linkedHashMap.get("hits")));
        event.setViews(views);
        log.info("For event id " + event.getId() + " has set views = " + event.getViews());

        event.setConfirmedRequests(confirmedRequests(event.getId()));
        log.info("For event id " + event.getId() + " has set confirmed requests = " + event.getConfirmedRequests());
        return event;
    }

    private Long confirmedRequests(Long eventId) {
        RequestStatus status = RequestStatus.CONFIRMED;
        List<Object[]> counts = requestRepository.getConfirmedRequestsByEvent(eventId, status);
        Long count = 0L;
        if (!counts.isEmpty()) {
            count = Long.parseLong(counts.get(0)[0].toString());
        }
        return count;
    }

    private ArrayList<LinkedHashMap> findViews(String uris) {
        String start = LocalDateTime.now().minusYears(50).format(FORMATTER);
        String end = LocalDateTime.now().plusYears(50).format(FORMATTER);
        ResponseEntity<Object> responseEntities = eventClient.getEndpointHits(start, end, uris, false);
        ArrayList<LinkedHashMap> listFromObject = new ArrayList<>();
        if (responseEntities != null) {
            listFromObject = (ArrayList<LinkedHashMap>) responseEntities.getBody();
        }
        return listFromObject;
    }

    private static EndpointHitEwmDto makeEndpointHitEwmDto(HttpServletRequest request) {
        EndpointHitEwmDto endpointHitEwmDto = new EndpointHitEwmDto();
        endpointHitEwmDto.setApp(NAME_OF_APP);
        endpointHitEwmDto.setUri(request.getRequestURI());
        endpointHitEwmDto.setIp(request.getRemoteAddr());
        endpointHitEwmDto.setTimestamp(LocalDateTime.now().format(FORMATTER));
        return endpointHitEwmDto;
    }

    public void validateEvent(Event event) {
        if (event.getAnnotation() == null || event.getAnnotation().isBlank()) {
            throw new BadRequestException("Annotation could not be empty.");
        }
        if (categoryRepository.findAll().contains(event.getAnnotation())) {
            throw new BadRequestException(
                    "Event " + event.getAnnotation() + " has already found in DB .");
        }
    }
}
