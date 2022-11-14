package ru.practicum.explorewithme.compilations.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.events.model.Event;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @Column(name = "compilation_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false, length = 255)
    private String title;
    @Column(name = "pinned", nullable = false)
    private Boolean pinned;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
