package com.example.itask.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.example.itask.controller.dto.EventDto;
import com.example.itask.controller.dto.ScoreDto;
import com.example.itask.persistance.model.Event;
import com.example.itask.persistance.model.Score;
import com.example.itask.persistance.repository.EventRepository;
import com.example.itask.service.EventService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private static final String SPLITERATOR_FOR_TEAMS = " vs ";
    private static final Pattern pattern = Pattern.compile("\\s* vs \\s*");

    private final EventRepository eventRepository;

    @Override
    public List<EventDto> getAllEvents() {
        return mapToEventDto(eventRepository.findAll());
    }

    @Override
    public EventDto getEventById(Long id) {
        Optional<Event> optionalEvent = eventRepository.findById(id);
        return optionalEvent.map(this::toEventDtoWithScores).orElse(null);
    }

    private EventDto toEventDtoWithScores(Event event) {
        EventDto eventDto = toEventDto(event);
        eventDto.setScores(mapToScoreDto(event.getItems()));
        return eventDto;
    }

    private List<ScoreDto> mapToScoreDto(Set<Score> items) {
        if (CollectionUtils.isEmpty(items)) {
            return List.of();
        }
        return items.stream()
                .filter(score -> Objects.nonNull(score) && Objects.nonNull(score.getScore()) && Objects.nonNull(score.getMinute()))
                .map(score -> {
                    ScoreDto scoreDto = new ScoreDto();
                    scoreDto.setMinute(score.getMinute());
                    scoreDto.setScore(score.getScore());
                    return scoreDto;
                })
                .sorted(Comparator.comparing(ScoreDto::getMinute).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public EventDto saveEvent(String eventName) {
        validateEventDto(eventName);
        return toEventDto(eventRepository.save(toEvent(eventName)));
    }

    private Event toEvent(String eventName) {
        Event event = new Event();
        String[] split = eventName.split(SPLITERATOR_FOR_TEAMS);
        event.setTeam1(split[0]);
        event.setTeam2(split[1]);
        return event;
    }

    private void validateEventDto(String eventName) {
        if (!StringUtils.hasText(eventName) || !pattern.matcher(eventName).find() || eventName.split(SPLITERATOR_FOR_TEAMS).length != 2) {
                throw new IllegalArgumentException("Event name is invalid");
        }
    }

    private List<EventDto> mapToEventDto(List<Event> events) {
        return events.stream()
                .filter(event -> event.getId() != null && event.getTeam1() != null && event.getTeam2() != null)
                .map(this::toEventDto)
                .collect(Collectors.toList());
    }

    private EventDto toEventDto(Event event) {
        return new EventDto(event.getId(), event.getTeam1() + SPLITERATOR_FOR_TEAMS + event.getTeam2());
    }
}
