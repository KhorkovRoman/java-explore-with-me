package ru.practicum.explorewithme.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.events.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {


}
