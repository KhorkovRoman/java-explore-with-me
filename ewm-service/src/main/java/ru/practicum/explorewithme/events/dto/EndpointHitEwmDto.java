package ru.practicum.explorewithme.events.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHitEwmDto {
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
