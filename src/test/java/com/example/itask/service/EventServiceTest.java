package com.example.itask.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.itask.controller.dto.EventDto;
import com.example.itask.controller.dto.ScoreDto;
import com.example.itask.persistance.model.Event;
import com.example.itask.persistance.model.Score;
import com.example.itask.persistance.repository.EventRepository;
import com.example.itask.service.impl.EventServiceImpl;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    EventRepository eventRepository;

    @InjectMocks
    EventServiceImpl eventService;

    @Test
    void getAllEventsTest_empty() {
        Mockito.when(eventRepository.findAll()).thenReturn(List.of());
        List<EventDto> allEvents = eventService.getAllEvents();

        assertThat(allEvents)
                .isNotNull()
                .isEmpty();
        verify(eventRepository, Mockito.times(1)).findAll();
    }

    @Test
    void getAllEventsTest() {
        Mockito.when(eventRepository.findAll()).thenReturn(List.of(getEvent(1L, "team 1", "team 2"), getEvent(2L, "team 3", "team 4")));
        List<EventDto> allEvents = eventService.getAllEvents();

        assertThat(allEvents)
                .isNotNull()
                .isNotEmpty()
                .hasSize(2)
                .containsExactlyInAnyOrder(new EventDto(1L, "team 1 vs team 2"), new EventDto(2L, "team 3 vs team 4"));
        verify(eventRepository, Mockito.times(1)).findAll();
    }

    @Test
    void getEventByIdTest_empty() {
        Long eventId = 1L;
        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.empty());
        EventDto result = eventService.getEventById(eventId);

        assertThat(result).isNull();
        verify(eventRepository, Mockito.times(1)).findById(eventId);
    }

    @Test
    void getEventByIdTest() {
        Long eventId = 1L;
        String score = "1-2";
        int scoreMinute = 1;

        Event event = getEvent(1L, "team 1", "team 2");
        Score score1 = new Score();
        score1.setScore(score);
        score1.setMinute(scoreMinute);
        event.setItems(Set.of(score1));
        Mockito.when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        EventDto result = eventService.getEventById(eventId);

        ScoreDto expectedScoreDto = new ScoreDto();
        expectedScoreDto.setScore(score);
        expectedScoreDto.setMinute(scoreMinute);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(eventId);
        assertThat(result.getName()).isEqualTo("team 1 vs team 2");
        assertThat(result.getScores())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        assertThat(result.getScores().get(0)).isEqualTo(expectedScoreDto);
        verify(eventRepository, Mockito.times(1)).findById(eventId);
    }

    @Test
    void saveEventByIdTest_invalidName() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            eventService.saveEvent("team1");
        });
        assertThat(thrown)
                .isNotNull()
                .hasMessage("Event name is invalid");
    }

    @Test
    void saveEventByIdTest_emptyName() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            eventService.saveEvent("   ");
        });
        assertThat(thrown)
                .isNotNull()
                .hasMessage("Event name is invalid");
    }

    @Test
    void saveEventByIdTest() {
        String eventName = "team 1 vs team 2";
        Event event = getEvent(1L, "team 1", "team 2");
        event.setItems(Set.of());
        when(eventRepository.save(any())).thenReturn(event);

        EventDto eventDto = eventService.saveEvent(eventName);

        assertThat(eventDto).isNotNull();
        assertThat(eventDto.getId()).isNotNull();
        assertThat(eventDto.getName()).isEqualTo(eventName);

        verify(eventRepository, Mockito.times(1)).save(any());
    }

    private Event getEvent(Long id, String team1, String team2) {
        Event event = new Event();
        event.setId(id);
        event.setTeam1(team1);
        event.setTeam2(team2);
        return event;
    }
}
