package com.pulserun.notification;

import com.pulserun.user.User;
import com.pulserun.user.UserRepository;
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
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));

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
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 알림입니다."));

        if (!notification.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("이 알림에 대한 권한이 없습니다.");
        }

        notification.markAsRead();
    }
}