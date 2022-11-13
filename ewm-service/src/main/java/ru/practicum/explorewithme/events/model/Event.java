package ru.practicum.explorewithme.events.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.categories.model.Category;
import ru.practicum.explorewithme.users.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    @ManyToOne
    @JoinColumn(name = "location_id")
    private LocationModel location;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @Enumerated(EnumType.STRING)
    private EventStatus state;
}
