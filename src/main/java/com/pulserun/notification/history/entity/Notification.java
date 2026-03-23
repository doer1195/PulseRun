package com.pulserun.notification.history.entity;

import com.pulserun.global.error.ErrorCode;
import com.pulserun.global.utils.Asserts;
import com.pulserun.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 500, nullable = false)
    private String message;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private boolean isRead = false;

    public void markAsRead() {
        this.isRead = true;
    }

    @Builder
    public Notification(User user, String message) {
        Asserts.notNull(user, ErrorCode.DATA_IS_NULL, "user");
        Asserts.hasText(message, ErrorCode.DATA_IS_BLANK);

        this.user = user;
        this.message = message;
    }
}
