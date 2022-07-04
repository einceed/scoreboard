package com.example.itask.service;

import com.example.itask.controller.dto.ScoreDto;

public interface PushNotificationService {
    void sendNotification(Long eventId, ScoreDto scoreDto);
}
