package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.dto.ViewStatsDto;
import ru.practicum.explorewithme.model.EndpointHit;

import java.util.List;

public interface StatsServerService {

    EndpointHit createEndpointHit(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> getEndpointHits(String start, String end, List<String> uris, Boolean unique);
}
