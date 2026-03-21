package com.pulserun.notification;

import com.pulserun.user.LoginUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<Slice<NotificationResponseDto>> getMyNotifications(
            @LoginUser Long userId,
            Pageable pageable) {

        Slice<Notification> notifications = notificationService.getMyNotifications(userId, pageable);

        Slice<NotificationResponseDto> response = notifications.map(NotificationResponseDto::from);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/unread-count")
    public ResponseEntity<UnreadCountResponseDto> getUnreadCount(@LoginUser Long userId) {
        long count = notificationService.getUnreadCount(userId);
        return ResponseEntity.ok(new UnreadCountResponseDto(count));
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Void> readNotification(
            @PathVariable Long notificationId,
            @LoginUser Long userId) {

        notificationService.readNotification(notificationId, userId);
        return ResponseEntity.ok().build();
    }
}