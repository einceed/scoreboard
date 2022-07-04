package com.example.itask.service;

import java.util.List;

import com.example.itask.controller.dto.EventDto;

public interface EventService {
    List<EventDto> getAllEvents();
    EventDto getEventById(Long id);
    EventDto saveEvent(String eventName);
}
