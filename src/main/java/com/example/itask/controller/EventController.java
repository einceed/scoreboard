package com.example.itask.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.itask.controller.dto.EventDto;
import com.example.itask.controller.dto.ScoreDto;
import com.example.itask.service.EventService;
import com.example.itask.service.ScoreService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final ScoreService scoreService;

    @GetMapping("/events")
    public List<EventDto> getEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/event/{id}")
    public EventDto getEventById(@PathVariable("id") Long id) {
        return eventService.getEventById(id);
    }

    @PostMapping("/event")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void saveEvent(@RequestParam("name") String eventName) {
        eventService.saveEvent(eventName);
    }

    @GetMapping("/event/{eventId}/score")
    public ScoreDto getLatestScoreByEventId(@PathVariable("eventId") Long eventId) {
        return scoreService.getLatestScoreByEventId(eventId);
    }

    @PostMapping("/event/{eventId}/score")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void getLatestScoreByEventId(@PathVariable("eventId") Long eventId,
                                        @RequestParam("minute") Integer minute,
                                        @RequestParam("score") String score) {
        scoreService.saveScore(eventId, minute, score);
    }

}
