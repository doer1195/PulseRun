package com.pulserun.notification.history.service;

import com.pulserun.global.error.ErrorCode;
import com.pulserun.global.error.exception.PulserunException;
import com.pulserun.notification.history.entity.Notification;
import com.pulserun.notification.history.repository.NotificationRepository;
import com.pulserun.user.entity.User;
import com.pulserun.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createNotification(Long receiverId, String message) {
        User receiver = userRepository.findById(receiverId)
                                      .orElseThrow(() -> new PulserunException(ErrorCode.USER_NOT_FOUND));

        Notification notification = Notification.builder()
                                                .user(receiver)
                                                .message(message)
                                                .build();

        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public Slice<Notification> getMyNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    @Transactional
    public void readNotification(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId)
                                                          .orElseThrow(() -> new PulserunException(ErrorCode.NOTIFICATION_NOT_FOUND));

        if (notification.getUser().getId().equals(userId)) {
            notification.markAsRead();
        }

        throw new PulserunException(ErrorCode.NOTIFICATION_NOT_ALLOWED);
    }
}