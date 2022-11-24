package ru.practicum.explorewithme.models.event;

import lombok.*;
import ru.practicum.explorewithme.models.category.Category;
import ru.practicum.explorewithme.models.compilation.Compilation;
import ru.practicum.explorewithme.models.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;
    @Column(name = "title", nullable = false, length = 120)
    private String title;
    @Column(name = "annotation", nullable = false, length = 2000)
    private String annotation;
    @Column(name = "description", nullable = false, length = 7000)
    private String description;
    @Column(name = "eventDate", nullable = false)
    private LocalDateTime eventDate;
    @Column(name = "participant_limit")
    private Long participantLimit;
    @Column(name = "paid")
    private Boolean paid;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @Column(name = "confirmed_requests")
    private Long confirmedRequests;
    @Column(name = "createdOn")
    private LocalDateTime createdOn;
    @Column(name = "publishedOn")
    private LocalDateTime publishedOn;
    @Column(name = "views")
    private Long views;
    @Column(name = "lat")
    private Float lat;
    @Column(name = "lon")
    private Float lon;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @Enumerated(EnumType.STRING)
    private EventStatus state;
    @ManyToMany(mappedBy = "events")
    @ToString.Exclude
    private Collection<Compilation> compilations;

    @Override
    public String toString() {
        Collection<Long> compIdCollection = getCompilations().stream()
                .map(Compilation::getId)
                .collect(Collectors.toList());
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", annotation='" + annotation + '\'' +
                ", description='" + description + '\'' +
                ", eventDate=" + eventDate +
                ", participantLimit=" + participantLimit +
                ", paid=" + paid +
                ", requestModeration=" + requestModeration +
                ", confirmedRequests=" + confirmedRequests +
                ", createdOn=" + createdOn +
                ", publishedOn=" + publishedOn +
                ", views=" + views +
                ", lat=" + lat +
                ", lon=" + lon +
                ", category=" + category +
                ", initiator=" + initiator +
                ", state=" + state +
                ", compilations=" + compIdCollection +
                '}';
    }
}
