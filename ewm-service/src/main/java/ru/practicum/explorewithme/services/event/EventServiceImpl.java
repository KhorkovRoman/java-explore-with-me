package ru.practicum.explorewithme.services.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dtos.endpointhit.EndpointHitEwmDto;
import ru.practicum.explorewithme.dtos.event.*;
import ru.practicum.explorewithme.models.category.Category;
import ru.practicum.explorewithme.repositories.CategoryRepository;
import ru.practicum.explorewithme.exeptions.BadRequestException;
import ru.practicum.explorewithme.exeptions.NotFoundException;
import ru.practicum.explorewithme.client.EventClient;
import ru.practicum.explorewithme.mappers.EventMapper;
import ru.practicum.explorewithme.models.event.Event;
import ru.practicum.explorewithme.models.event.EventStatus;
import ru.practicum.explorewithme.repositories.EventRepository;
import ru.practicum.explorewithme.models.request.RequestStatus;
import ru.practicum.explorewithme.repositories.RequestRepository;
import ru.practicum.explorewithme.models.user.User;
import ru.practicum.explorewithme.repositories.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EventServiceImpl implements EventService {

    private static final String NOT_FOUND_USER = "In DB has not found user id ";
    private static final String NOT_FOUND_CATEGORY = "In DB has not found category id ";
    private static final String NOT_FOUND_EVENT = "In DB has not found event id ";
    private static final String OBJECT_EMPTY = "Object can't be empty. Need the object: ";

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
        if (newEventDto == null) {
            throw new IllegalArgumentException(OBJECT_EMPTY + "New Event.");
        }
        Event event = EventMapper.toEvent(newEventDto);
        validateEvent(event);
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_CATEGORY + newEventDto.getCategory()));
        event.setCategory(category);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER + userId));
        event.setInitiator(user);

        event.setLon(newEventDto.getLocation().getLon());
        event.setLat(newEventDto.getLocation().getLat());

        event.setState(EventStatus.PENDING);

        Event eventDB = eventRepository.save(event);
        log.info("Event id " + eventDB.getId() + " has successfully created.");
        return eventDB;
    }

    private Event updateParamEventByUser(Event event, UpdateEventRequest updateEventRequest) {
        if (updateEventRequest == null) {
            throw new IllegalArgumentException(OBJECT_EMPTY + "Update Event Request.");
        }
        validateEvent(event);
        if (updateEventRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getCategory() != null) {
            Category category = categoryRepository.findById(updateEventRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException(NOT_FOUND_CATEGORY + updateEventRequest.getCategory()));
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
        if (updateEventRequest == null) {
            throw new IllegalArgumentException(OBJECT_EMPTY + "Update Event Request.");
        }
        Long eventId = updateEventRequest.getEventId();
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_EVENT + eventId));
        Event eventDB = eventRepository.save(updateParamEventByUser(event, updateEventRequest));
        log.info("Event id " + eventDB.getId() + " has successfully updated.");
        return eventDB;
    }

    private Event updateParamEventByAdmin(Event event, AdminUpdateEventRequest adminUpdateEventRequest) {
        if (adminUpdateEventRequest == null) {
            throw new IllegalArgumentException(OBJECT_EMPTY + "Admin Update Event Request.");
        }
        if (adminUpdateEventRequest.getAnnotation() != null) {
            event.setAnnotation(adminUpdateEventRequest.getAnnotation());
        }
        if (adminUpdateEventRequest.getCategory() != null) {
            Category category = categoryRepository.findById(adminUpdateEventRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException(
                            NOT_FOUND_CATEGORY + adminUpdateEventRequest.getCategory()));
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
        if (adminUpdateEventRequest == null) {
            throw new IllegalArgumentException(OBJECT_EMPTY + "Admin Update Event Request.");
        }
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_EVENT + eventId));
        Event eventDB = eventRepository.save(updateParamEventByAdmin(event, adminUpdateEventRequest));
        log.info("Event id " + eventDB.getId() + " has successfully updated.");
        return eventDB;
    }

    @Override
    public Event cancelEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_EVENT + eventId));
        event.setState(EventStatus.CANCELED);
        Event eventDB = eventRepository.save(event);
        log.info("Event id " + eventDB.getId() + " has successfully canceled.");
        return eventDB;
    }

    @Override
    public Event publishEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_EVENT + eventId));
        event.setState(EventStatus.PUBLISHED);
        Event eventDB = eventRepository.save(event);
        log.info("Event id " + eventDB.getId() + " has successfully published.");
        return eventDB;
    }

    @Override
    public Event rejectEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_EVENT + eventId));
        event.setState(EventStatus.CANCELED);
        Event eventDB = eventRepository.save(event);
        log.info("Event id " + eventDB.getId() + " has successfully rejected.");
        return eventDB;
    }

    @Override
    public Collection<EventShortDto> getAllEventsByUser(Long userId, PageRequest pageRequest) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER + userId));
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
        if (rangeStart.isBlank()) {
            rangeStartFormatted = LocalDateTime.now();
        } else {
            rangeStartFormatted = LocalDateTime.parse(rangeStart, FORMATTER);
        }
        LocalDateTime rangeEndFormatted;
        if (rangeEnd.isBlank()) {
            rangeEndFormatted = LocalDateTime.now();
        } else {
            rangeEndFormatted = LocalDateTime.parse(rangeEnd, FORMATTER);
        }

        List<Category> categoryEntities = categoryRepository.getCategoriesFromIds(categories);
        List<User> userEntities = userRepository.getUsersFromIds(users);

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
        if (rangeStart.isBlank()) {
            rangeStartFormatted = LocalDateTime.now();
        } else {
            rangeStartFormatted = LocalDateTime.parse(rangeStart, FORMATTER);
        }
        LocalDateTime rangeEndFormatted;
        if (rangeEnd.isBlank()) {
            rangeEndFormatted = LocalDateTime.now().plusYears(50);
        } else {
            rangeEndFormatted = LocalDateTime.parse(rangeEnd, FORMATTER);
        }

        eventClient.createEndpointHit(makeEndpointHitEwmDto(request));

        List<Category> categoryEntities = categoryRepository.getCategoriesFromIds(categories);

        Page<Event> events = null;
        if (text.isBlank()) {
            if (onlyAvailable.equals(true)) {
                if (EVENT_DATE.equals(sort) || sort.isBlank()) {
                    events = eventRepository.getAllEventsPublicByEventDateAvailableAllText(categoryEntities, paid,
                            rangeStartFormatted, rangeEndFormatted, pageRequest);
                }
                if (VIEWS.equals(sort)) {
                    events = eventRepository.getAllEventsPublicByViewsAvailableAllText(categoryEntities, paid,
                            rangeStartFormatted, rangeEndFormatted, pageRequest);
                }
            }
        } else {
            if (onlyAvailable.equals(true)) {
                if (EVENT_DATE.equals(sort) || sort.isBlank()) {
                    events = eventRepository.getAllEventsPublicByEventDateAvailable(text, categoryEntities, paid,
                            rangeStartFormatted, rangeEndFormatted, pageRequest);
                }
                if (VIEWS.equals(sort)) {
                    events = eventRepository.getAllEventsPublicByViewsAvailable(text, categoryEntities, paid,
                            rangeStartFormatted, rangeEndFormatted, pageRequest);
                }
            }
        }

        if (text.isBlank()) {
            if (EVENT_DATE.equals(sort) || sort.isBlank()) {
                events = eventRepository.getAllEventsPublicByEventDateAllText(categoryEntities, paid,
                        rangeStartFormatted, rangeEndFormatted, pageRequest);
            }
            if (VIEWS.equals(sort)) {
                events = eventRepository.getAllEventsPublicByViewsAllText(categoryEntities, paid,
                        rangeStartFormatted, rangeEndFormatted, pageRequest);
            }
        } else {
            if (EVENT_DATE.equals(sort) || sort.isBlank()) {
                events = eventRepository.getAllEventsPublicByEventDate(text, categoryEntities, paid,
                        rangeStartFormatted, rangeEndFormatted, pageRequest);
            }
            if (VIEWS.equals(sort)) {
                events = eventRepository.getAllEventsPublicByViews(text, categoryEntities, paid,
                        rangeStartFormatted, rangeEndFormatted, pageRequest);
            }
        }

        List<EventShortDto> eventList = Collections.emptyList();
        if (events != null) {
            eventList = EventMapper.toEventShortDtoCollection(events.getContent());
        }

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

        ArrayList<LinkedHashMap<Object, Object>> listOfStats = findViews(uris);
        if (!listOfStats.isEmpty()) {
            Map<Long, Long> mapEventHits = new HashMap<>();
            for (LinkedHashMap<Object, Object> mapFromStatsOfEventHits : listOfStats) {
                String uriForSplit = String.valueOf(mapFromStatsOfEventHits.get("uri"));
                String[] splitOfUri = uriForSplit.split("/");
                Long eventId = Long.parseLong(splitOfUri[2]);
                Long hits = Long.parseLong(String.valueOf(mapFromStatsOfEventHits.get("hits")));

                mapEventHits.put(eventId, hits);
            }
            for (EventShortDto event : eventList) {
                Set keys = mapEventHits.keySet();
                if (keys.contains(event.getId())) {
                    Long views = mapEventHits.get(event.getId());
                    event.setViews(views);
                    log.info("For event id " + event.getId() + " has set views = " + event.getViews());
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



        return eventList;
    }

    @Override
    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_EVENT + eventId));
    }

    @Override
    public Event getEventByIdPublic(Long id, HttpServletRequest request) {
        eventClient.createEndpointHit(makeEndpointHitEwmDto(request));
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_EVENT + id));

        String uris = request.getRequestURI();
        ArrayList<LinkedHashMap<Object, Object>> listOfStats = findViews(uris);
        LinkedHashMap<Object, Object> mapFromStatsOfEventHits = listOfStats.get(0);
        Long views = Long.parseLong(String.valueOf(mapFromStatsOfEventHits.get("hits")));

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

    private ArrayList<LinkedHashMap<Object, Object>> findViews(String uris) {
        String start = LocalDateTime.now().minusYears(50).format(FORMATTER);
        String end = LocalDateTime.now().plusYears(50).format(FORMATTER);
        ResponseEntity<Object> responseEntities = eventClient.getEndpointHits(start, end, uris, false);
        ArrayList<LinkedHashMap<Object, Object>> listFromObject = new ArrayList<>();
        if (responseEntities != null) {
            listFromObject = (ArrayList<LinkedHashMap<Object, Object>>) responseEntities.getBody();
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
        if (event == null) {
            throw new IllegalArgumentException(OBJECT_EMPTY + "Event.");
        }
        if (event.getAnnotation() == null || event.getAnnotation().isBlank()) {
            throw new BadRequestException("Annotation could not be empty.");
        }
        if (categoryRepository.findAll().contains(event.getAnnotation())) {
            throw new BadRequestException(
                    "Event " + event.getAnnotation() + " has already found in DB .");
        }
    }
}
