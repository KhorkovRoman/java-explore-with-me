package ru.practicum.explorewithme.compilations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.compilations.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {



}
