package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsServerRepository extends JpaRepository<EndpointHit, Long> {

    @Query(value = "select app, uri, count(distinct ip) hits " +
            " from endpoint_hits " +
            " where timestamp between ?1 and ?2" +
            "       and uri in ?3" +
            " group by app, uri",
            nativeQuery = true)
    List<Object[]> getEndpointHitsUnique(LocalDateTime startFormatted, LocalDateTime endFormatted,
                                         List<String> uris);

    @Query(value = "select app, uri, count(ip) hits " +
            " from endpoint_hits " +
            " where timestamp between ?1 and ?2" +
            "       and uri in ?3" +
            " group by app, uri",
            nativeQuery = true)
    List<Object[]> getEndpointHitsNotUnique(LocalDateTime startFormatted, LocalDateTime endFormatted,
                                            List<String> uris);
}
