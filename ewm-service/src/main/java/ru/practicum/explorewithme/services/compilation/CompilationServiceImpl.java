package ru.practicum.explorewithme.services.compilation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.exeptions.BadRequestException;
import ru.practicum.explorewithme.exeptions.NotFoundException;
import ru.practicum.explorewithme.dtos.compilation.NewCompilationDto;
import ru.practicum.explorewithme.mappers.CompilationMapper;
import ru.practicum.explorewithme.models.compilation.Compilation;
import ru.practicum.explorewithme.repositories.CompilationRepository;
import ru.practicum.explorewithme.models.event.Event;
import ru.practicum.explorewithme.repositories.EventRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CompilationServiceImpl implements CompilationService {

    private static final String NOT_FOUND_COMPILATION = "In DB has not found compilation id ";
    private static final String NOT_FOUND_EVENT = "In DB has not found event id ";
    private static final String OBJECT_EMPTY = "Object can't be empty. Need the object: ";

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CompilationServiceImpl(CompilationRepository compilationRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Compilation createCompilation(NewCompilationDto newCompilationDto) {
        if (newCompilationDto == null) {
            throw new IllegalArgumentException(OBJECT_EMPTY + "New Compilation.");
        }
        Compilation compilation = compilationRepository.save(CompilationMapper.toCompilation(newCompilationDto));
        validateCompilation(compilation);
        Collection<Long> eventsList = newCompilationDto.getEvents();
        Collection<Event> events = new ArrayList<>();
        for (Long eventId: eventsList) {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new NotFoundException(NOT_FOUND_EVENT + eventId));
            events.add(event);
        }
        compilation.setEvents(events);
        compilationRepository.save(compilation);

        log.info("Compilation id " + compilation.getId() + " has successfully created.");
        return compilation;
    }

    @Override
    public void addEventToCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COMPILATION + compId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_EVENT + eventId));
        Collection<Event> events = compilation.getEvents();
        events.add(event);
        compilation.setEvents(events);
        compilationRepository.save(compilation);
        log.info("Event id " + eventId + " was successfully added to Compilation with id " +
                compilation.getId() + ".");
    }

    @Override
    public void deleteEventFromCompilation(Long compId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COMPILATION + compId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_EVENT + eventId));
        Collection<Event> events = compilation.getEvents();
        events.remove(event);
        compilation.setEvents(events);
        compilationRepository.save(compilation);
        log.info("Event id " + eventId + " has successfully deleted from Compilation id " +
                compilation.getId() + ".");
    }

    @Override
    public Collection<Compilation> getAllCompilations(Boolean pinned, PageRequest pageRequest) {
        return compilationRepository.getAllCompilationsByPage(pinned, pageRequest).stream()
                .collect(Collectors.toList());
    }

    @Override
    public Compilation getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COMPILATION + compId));
        log.info("Compilation id " + compId + " has found in DB.");
        return compilation;
    }

    @Override
    public void deleteCompilation(Long compId) {
        compilationRepository.deleteById(compId);
        log.info("Compilation id " + compId + " has successfully deleted.");
    }

    @Override
    public void pinCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COMPILATION + compId));
        log.info("Compilation id" + compId + " has successfully found in DB.");
        compilation.setPinned(true);
        compilationRepository.save(compilation);
        log.info("Compilation id " + compId + " has successfully pinned.");
    }

    @Override
    public void delPinCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException(NOT_FOUND_COMPILATION + compId));
        log.info("Compilation id " + compId + " has successfully found in DB.");
        compilation.setPinned(false);
        compilationRepository.save(compilation);
        log.info("Compilation id " + compId + " has successfully unpinned.");
    }

    public void validateCompilation(Compilation compilation) {
        if (compilation == null) {
            throw new IllegalArgumentException(OBJECT_EMPTY + "Compilation.");
        }
        if (compilation.getTitle() == null || compilation.getTitle().isBlank()) {
            throw new BadRequestException("Name could not be empty.");
        }
        if (compilationRepository.findAll().contains(compilation.getTitle())) {
            throw new BadRequestException("Compilation " + compilation.getTitle() + " has already found in DB .");
        }
    }
}
