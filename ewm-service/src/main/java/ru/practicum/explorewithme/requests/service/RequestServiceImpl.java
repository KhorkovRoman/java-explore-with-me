package ru.practicum.explorewithme.requests.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.events.model.Event;
import ru.practicum.explorewithme.events.repository.EventRepository;
import ru.practicum.explorewithme.exeption.ValidationException;
import ru.practicum.explorewithme.requests.model.Request;
import ru.practicum.explorewithme.requests.model.RequestStatus;
import ru.practicum.explorewithme.requests.repository.RequestRepository;
import ru.practicum.explorewithme.users.model.User;
import ru.practicum.explorewithme.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

@Slf4j
@Service
public class RequestServiceImpl implements RequestService {

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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "In DB has no user id " + userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "In DB has no event id " + eventId));
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
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "In DB has no request id " + reqId));
        request.setStatus(RequestStatus.CANCELED);
        return requestRepository.save(request);
    }

    @Override
    public Collection<Request> getAllRequestsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "In DB has no user id " + userId));
        return requestRepository.getAllRequestsByUser(user.getId());
    }

    @Override
    public Collection<Request> getAllRequestsByEvent(Long userId, Long eventId) {
        return requestRepository.getAllRequestsOnEventByUser(userId, eventId);
    }

    @Override
    public Request confirmRequest(Long userId, Long eventId, Long reqId) {
        Request request = requestRepository.findById(reqId)
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "In DB has no request id " + reqId));
        request.setStatus(RequestStatus.CONFIRMED);
        log.info("Has changed status on " + request.getStatus() + " for request id " + reqId);
        return requestRepository.save(request);
    }

    @Override
    public Request rejectRequest(Long userId, Long eventId, Long reqId) {
        Request request = requestRepository.findById(reqId)
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "In DB has no request with id " + reqId));
        request.setStatus(RequestStatus.REJECTED);
        log.info("Has changed status on " + request.getStatus() + " for request id " + reqId);
        return requestRepository.save(request);
    }
}
