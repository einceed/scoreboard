package com.example.itask.service;

import com.example.itask.controller.dto.ScoreDto;

public interface ScoreService {
    ScoreDto getLatestScoreByEventId(Long eventId);
    void saveScore(Long eventId, Integer minute, String score);
}
