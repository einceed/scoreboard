package com.example.itask.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.itask.controller.dto.ScoreDto;
import com.example.itask.persistance.model.Event;
import com.example.itask.persistance.model.Score;
import com.example.itask.persistance.repository.EventRepository;
import com.example.itask.persistance.repository.ScoreRepository;
import com.example.itask.service.impl.ScoreServiceImpl;

@ExtendWith(MockitoExtension.class)
class ScoreServiceTest {

    @Mock
    ScoreRepository scoreRepository;

    @Mock
    EventRepository eventRepository;

    @InjectMocks
    ScoreServiceImpl scoreService;

    @Test
    void getLatestScoreByEventIdTest_eventIdIsNull() {

        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            scoreService.getLatestScoreByEventId(null);
        });
        assertThat(thrown)
                .isNotNull()
                .hasMessage("eventId is null");
    }

    @Test
    void getLatestScoreByEventIdTest_eventIsAbsent() {

        Long eventId = 1L;
        Mockito.when(scoreRepository.getFirstByEventIdOrderByMinuteDesc(eventId)).thenReturn(Optional.empty());
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            scoreService.getLatestScoreByEventId(eventId);
        });
        assertThat(thrown)
                .isNotNull()
                .hasMessage("Score is not found for eventId: 1");
        Mockito.verify(scoreRepository, Mockito.times(1)).getFirstByEventIdOrderByMinuteDesc(eventId);
    }

    @Test
    void getLatestScoreByEventIdTest() {
        Long eventId = 1L;
        String score = "1:0";
        int minute = 3;

        Mockito.when(scoreRepository.getFirstByEventIdOrderByMinuteDesc(eventId)).thenReturn(Optional.of(getScore(eventId, score, minute)));
        ScoreDto scoreDto = scoreService.getLatestScoreByEventId(eventId);

        assertThat(scoreDto).isNotNull().isEqualTo(getScoreDto(eventId, score, minute));
        Mockito.verify(scoreRepository, Mockito.times(1)).getFirstByEventIdOrderByMinuteDesc(eventId);
    }

    @Test
    void saveScoreTest_eventIdIsNull() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            scoreService.saveScore(null, null, null);
        });
        assertThat(thrown)
                .isNotNull()
                .hasMessage("eventId is null");
    }

    @Test
    void saveScoreTest_minuteIsNull() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            scoreService.saveScore(1L, null, null);
        });
        assertThat(thrown)
                .isNotNull()
                .hasMessage("minute is null");
    }

    @Test
    void saveScoreTest_scoreIsNull() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            scoreService.saveScore(1L, 3, null);
        });
        assertThat(thrown)
                .isNotNull()
                .hasMessage("score is null");
    }

    @Test
    void saveScoreTest_scoreIsInvalid() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            scoreService.saveScore(1L, 3, "2:3");
        });
        assertThat(thrown)
                .isNotNull()
                .hasMessage("score is invalid");
    }

    @Test
    void saveScoreTest_eventIsAbsent() {
        Long eventId = 1L;
        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.empty());
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            scoreService.saveScore(eventId, 3, "2-3");
        });
        assertThat(thrown)
                .isNotNull()
                .hasMessage("Event is not found for eventId: " + eventId);
    }

    @Test
    void saveScoreTest() {
        Long eventId = 1L;
        String score = "1-0";
        int minute = 3;

        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.of(getEvent(eventId)));

        scoreService.saveScore(eventId, minute, score);
        Mockito.verify(scoreRepository, Mockito.times(1)).save(any());
    }

    private Score getScore(Long eventId, String score, Integer minute) {
        Score score1 = new Score();
        score1.setScore(score);
        Event event = getEvent(eventId);
        score1.setEvent(event);
        score1.setMinute(minute);
        return score1;
    }

    private Event getEvent(Long eventId) {
        Event event = new Event();
        event.setId(eventId);
        return event;
    }

    private ScoreDto getScoreDto(Long eventId, String score, Integer minute) {
        ScoreDto scoreDto = new ScoreDto();
        scoreDto.setEventId(eventId);
        scoreDto.setScore(score);
        scoreDto.setMinute(minute);
        return scoreDto;
    }
}
