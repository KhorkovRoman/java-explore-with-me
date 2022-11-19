package ru.practicum.explorewithme.compilations.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.compilations.dto.NewCompilationDto;
import ru.practicum.explorewithme.compilations.mapper.CompilationMapper;
import ru.practicum.explorewithme.compilations.model.Compilation;
import ru.practicum.explorewithme.compilations.repository.CompilationRepository;
import ru.practicum.explorewithme.events.model.Event;
import ru.practicum.explorewithme.events.repository.EventRepository;
import ru.practicum.explorewithme.exeption.ValidationException;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Autowired
    public CompilationServiceImpl(CompilationRepository compilationRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public Compilation createCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = compilationRepository.save(CompilationMapper.toCompilation(newCompilationDto));
        Collection<Long> eventsList = newCompilationDto.getEvents();
        Collection<Event> events = new ArrayList<>();
        for(Long eventId: eventsList) {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                            "In DB has no event id " + eventId));
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
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "In DB has no comilation with id " + compId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "In DB has no event with id " + eventId));
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
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "In DB has no compilation id " + compId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "In DB has no event id " + eventId));
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
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "In DB has no compilation id " + compId));
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
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "In DB has no compilation id " + compId));
        log.info("Compilation id" + compId + " has successfully found in DB.");
        compilation.setPinned(true);
        compilationRepository.save(compilation);
        log.info("Compilation id " + compId + " has successfully pinned.");
    }

    @Override
    public void delPinCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new ValidationException(HttpStatus.NOT_FOUND,
                        "In DB has no compilation id " + compId));
        log.info("Compilation id " + compId + " has successfully found in DB.");
        compilation.setPinned(false);
        compilationRepository.save(compilation);
        log.info("Compilation id " + compId + " has successfully unpinned.");
    }
}
