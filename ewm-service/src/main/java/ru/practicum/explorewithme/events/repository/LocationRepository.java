package ru.practicum.explorewithme.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.events.model.LocationModel;

public interface LocationRepository extends JpaRepository<LocationModel, Long> {
}
