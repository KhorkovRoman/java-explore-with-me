package ru.practicum.explorewithme.services.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.exeptions.BadRequestException;
import ru.practicum.explorewithme.models.event.Event;
import ru.practicum.explorewithme.repositories.EventRepository;
import ru.practicum.explorewithme.exeptions.NotFoundException;
import ru.practicum.explorewithme.models.request.Request;
import ru.practicum.explorewithme.models.request.RequestStatus;
import ru.practicum.explorewithme.repositories.RequestRepository;
import ru.practicum.explorewithme.models.user.User;
import ru.practicum.explorewithme.repositories.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

@Slf4j
@Service
public class RequestServiceImpl implements RequestService {

    private static final String NOT_FOUND_REQUEST = "In DB has not found request id ";
    private static final String NOT_FOUND_USER = "In DB has not found user id ";
    private static final String NOT_FOUND_EVENT = "In DB has not found event id ";
    private static final String BAD_REQUEST = "Event id could not be empty.";

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository,
                              UserRepository userRepository,
                              EventRepository eventRepository) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Request createRequest(Long userId, Long eventId) {
        if (eventId == null) {
            throw new BadRequestException(BAD_REQUEST);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER + userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_EVENT + eventId));
        Request request = new Request();
        request.setRequester(user);
        request.setEvent(event);
        request.setStatus(RequestStatus.PENDING);
        request.setCreated(LocalDateTime.now());
        return requestRepository.save(request);
    }

    @Override
    public Request cancelRequest(Long userId, Long reqId) {
        Request request = requestRepository.findById(reqId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_REQUEST + reqId));
        request.setStatus(RequestStatus.CANCELED);
        return requestRepository.save(request);
    }

    @Override
    public Collection<Request> getAllRequestsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_USER + userId));
        return requestRepository.getAllRequestsByUser(user.getId());
    }

    @Override
    public Collection<Request> getAllRequestsByEvent(Long userId, Long eventId) {
        return requestRepository.getAllRequestsOnEventByUser(userId, eventId);
    }

    @Override
    public Request confirmRequest(Long userId, Long eventId, Long reqId) {
        Request request = requestRepository.findById(reqId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_REQUEST + reqId));
        request.setStatus(RequestStatus.CONFIRMED);
        log.info("Has changed status on " + request.getStatus() + " for request id " + reqId);
        return requestRepository.save(request);
    }

    @Override
    public Request rejectRequest(Long userId, Long eventId, Long reqId) {
        Request request = requestRepository.findById(reqId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_REQUEST + reqId));
        request.setStatus(RequestStatus.REJECTED);
        log.info("Has changed status on " + request.getStatus() + " for request id " + reqId);
        return requestRepository.save(request);
    }
}
