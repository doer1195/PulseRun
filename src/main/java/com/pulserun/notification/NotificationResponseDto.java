package com.pulserun.notification;

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