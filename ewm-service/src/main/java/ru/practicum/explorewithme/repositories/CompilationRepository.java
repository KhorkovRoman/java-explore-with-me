package ru.practicum.explorewithme.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.models.compilation.Compilation;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Query("select c " +
            "from Compilation c " +
            "where (c.pinned = ?1) " +
            "group by c.id " +
            "order by count(distinct c.id) desc")
    Page<Compilation> getAllCompilationsByPage(Boolean pinned, PageRequest pageRequest);

}
