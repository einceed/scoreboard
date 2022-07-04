package com.example.itask.service.impl;

import static com.example.itask.config.WebSocketDefinition.WS_OUTBOUND_CHANEL;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.itask.controller.dto.ScoreDto;
import com.example.itask.service.PushNotificationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PushNotificationServiceImpl implements PushNotificationService {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Override
    @Async("asyncExecutor")
    public void sendNotification(Long eventId, ScoreDto scoreDto) {
        simpMessagingTemplate.convertAndSendToUser(String.valueOf(eventId), WS_OUTBOUND_CHANEL, scoreDto);

    }
}
