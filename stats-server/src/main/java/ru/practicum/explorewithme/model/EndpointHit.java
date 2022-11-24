package ru.practicum.explorewithme.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "endpoint_hits")
public class EndpointHit {
    @Id
    @Column(name = "endpoint_hits_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "app", nullable = false, length = 255)
    private String app;
    @Column(name = "uri", nullable = false, length = 255)
    private String uri;
    @Column(name = "ip", nullable = false, length = 255)
    private String ip;
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
}
