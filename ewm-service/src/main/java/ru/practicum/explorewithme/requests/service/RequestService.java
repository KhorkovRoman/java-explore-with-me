package ru.practicum.explorewithme.requests.service;

import ru.practicum.explorewithme.requests.model.Request;

import java.util.Collection;

public interface RequestService {

    Request createRequest(Long userId, Long eventId);

    Request cancelRequest(Long userId, Long requestId);

    Collection<Request> getAllRequestsByUser(Long userId);

    Collection<Request> getAllRequestsByEvent(Long userId, Long eventId);

    Request confirmRequest(Long userId, Long eventId, Long reqId);

    Request rejectRequest(Long userId, Long eventId, Long reqId);

}
