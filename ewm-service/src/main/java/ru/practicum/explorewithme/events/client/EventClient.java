package ru.practicum.explorewithme.events.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.explorewithme.client.BaseClient;
import ru.practicum.explorewithme.events.dto.EndpointHitEwmDto;

import java.util.Map;

@Slf4j
@Service
public class EventClient extends BaseClient {
    private static final String API_PREFIX = "";

    @Autowired
    public EventClient(@Value("http://localhost:9090") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> createEndpointHit(EndpointHitEwmDto endpointHitEwmDto) {
        return post("/hit", null, null, endpointHitEwmDto);
    }

    public ResponseEntity<Object> getEndpointHits(String start, String end, String uris, Boolean unique) {
        Map<String, Object> parameters = Map.of(
                "start", start,
                "end", end,
                "uris", uris,
                "unique", unique
        );
        return get("/stats" + "?start={start}&end={end}&uris={uris}&unique={unique}",null, parameters);
    }
}
