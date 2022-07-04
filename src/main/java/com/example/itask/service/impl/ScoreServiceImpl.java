package com.example.itask.service.impl;

import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.itask.controller.dto.ScoreDto;
import com.example.itask.persistance.model.Event;
import com.example.itask.persistance.model.Score;
import com.example.itask.persistance.repository.EventRepository;
import com.example.itask.persistance.repository.ScoreRepository;
import com.example.itask.service.PushNotificationService;
import com.example.itask.service.ScoreService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {

    private static final Pattern pattern = Pattern.compile("\\d{1,2}-\\d{1,2}");

    private final ScoreRepository scoreRepository;
    private final EventRepository eventRepository;
    private final PushNotificationService pushNotificationService;

    @Override
    public ScoreDto getLatestScoreByEventId(Long eventId) {
        if (Objects.isNull(eventId)) {
            throw new IllegalArgumentException("eventId is null");
        }
        Optional<Score> optionalScore = scoreRepository.getFirstByEventIdOrderByMinuteDesc(eventId);
        if (optionalScore.isEmpty()) {
            throw new IllegalArgumentException("Score is not found for eventId: " + eventId);
        }
        return toScoreDto(optionalScore.get());
    }

    @Override
    public void saveScore(Long eventId, Integer minute, String score) {
        validateScore(eventId, minute, score);
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            throw new IllegalArgumentException("Event is not found for eventId: " + eventId);
        }
        scoreRepository.save(toScore(minute, score, event.get()));
        pushNotificationService.sendNotification(eventId, toScoreDto(toScore(minute, score, event.get())));
    }

    private Score toScore(Integer minute, String actualScore, Event event) {
        Score score = new Score();
        score.setScore(actualScore);
        score.setMinute(minute);
        score.setEvent(event);
        return score;
    }

    private void validateScore(Long eventId, Integer minute, String score) {
        if (Objects.isNull(eventId)) {
            throw new IllegalArgumentException("eventId is null");
        }
        if (Objects.isNull(minute)) {
            throw new IllegalArgumentException("minute is null");
        }
        if (!StringUtils.hasText(score)) {
            throw new IllegalArgumentException("score is null");
        }
        if (!pattern.matcher(score).matches()) {
            throw new IllegalArgumentException("score is invalid");
        }
    }

    private ScoreDto toScoreDto(Score score) {
        ScoreDto scoreDto = new ScoreDto();
        scoreDto.setScore(score.getScore());
        scoreDto.setMinute(score.getMinute());
        scoreDto.setEventId(score.getEvent().getId());
        return scoreDto;
    }

}
