package ru.practicum.explorewithme.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.dto.ViewStatsDto;
import ru.practicum.explorewithme.mapper.EndpointHitMapper;
import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.repository.StatsServerRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class StatsServerServiceImpl implements StatsServerService {

    private static DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final StatsServerRepository statsServerRepository;

    @Autowired
    public StatsServerServiceImpl(StatsServerRepository statsServerRepository) {
        this.statsServerRepository = statsServerRepository;
    }

    @Override
    public EndpointHit createEndpointHit(EndpointHitDto endpointHitDto) {
        EndpointHit endpointHit = statsServerRepository.save(EndpointHitMapper.toEndpointHit(endpointHitDto));
        log.info("EndpointHit id " + endpointHit.getId() + " has successfully created.");
        return endpointHit;
    }

    @Override
    public List<ViewStatsDto> getEndpointHits(String start, String end, List<String> uris, Boolean unique) {

        LocalDateTime startFormatted = LocalDateTime.parse(start, FORMATTER);
        LocalDateTime endFormatted = LocalDateTime.parse(end, FORMATTER);

        List<Object[]> endpointHits = new ArrayList<>();
        if(unique.equals(true)) {
            endpointHits = statsServerRepository.getEndpointHitsUnique(startFormatted, endFormatted, uris);
        } else {
            endpointHits = statsServerRepository.getEndpointHitsNotUnique(startFormatted, endFormatted, uris);
        }
        List<ViewStatsDto> viewStatsDtos = new ArrayList<>();

        if (!endpointHits.isEmpty()) {
            for (Object[] object : endpointHits) {
                ViewStatsDto viewStatsDto = new ViewStatsDto();
                viewStatsDto.setApp(object[0].toString());
                viewStatsDto.setUri(object[1].toString());
                viewStatsDto.setHits(Long.valueOf(object[2].toString()));
                viewStatsDtos.add(viewStatsDto);
            }
        }
        return viewStatsDtos;
    }
}
