package com.pulserun.notification.history.dto;

import com.pulserun.notification.history.entity.Notification;

import java.time.LocalDateTime;

public record NotificationResponseDto(
        Long id,
        String message,
        LocalDateTime createdAt,
        boolean isRead
) {
    public static NotificationResponseDto from(Notification notification) {
        return new NotificationResponseDto(
                notification.getId(),
                notification.getMessage(),
                notification.getCreatedAt(),
                notification.isRead()
        );
    }
}