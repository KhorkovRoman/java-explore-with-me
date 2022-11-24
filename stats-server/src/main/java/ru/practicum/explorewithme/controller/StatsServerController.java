package ru.practicum.explorewithme.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.dto.ViewStatsDto;
import ru.practicum.explorewithme.mapper.EndpointHitMapper;
import ru.practicum.explorewithme.service.StatsServerService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "")
public class StatsServerController {

    private final StatsServerService statsServerService;

    @Autowired
    public StatsServerController(StatsServerService statsServerService) {
        this.statsServerService = statsServerService;
    }

    @PostMapping("/hit")
    public EndpointHitDto createEndpointHit(@RequestBody EndpointHitDto endpointHitDto) {
        log.info("Has received request to endpoint POST/hit");
        return EndpointHitMapper.toEndpointHitDto(statsServerService.createEndpointHit(endpointHitDto));
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getEndpointHits(@RequestParam(required = true) String start,
                                              @RequestParam(required = true) String end,
                                              @RequestParam List<String> uris,
                                              @RequestParam (defaultValue = "false") Boolean unique) {
        log.info("Has received request to endpoint GET/stats?start={}end{}uris={}unique{}",
                                          start, end, uris, unique);
        List<ViewStatsDto> viewStatsDtos = statsServerService.getEndpointHits(start, end, uris, unique);
        return new ResponseEntity<>(viewStatsDtos, HttpStatus.OK);
    }
}
