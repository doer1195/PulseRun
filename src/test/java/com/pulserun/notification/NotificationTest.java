package com.pulserun.notification;

import com.pulserun.global.error.exception.PulserunException;
import com.pulserun.notification.history.entity.Notification;
import com.pulserun.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NotificationTest {

    @Test
    @DisplayName("정상적인 알람 생성")
    void createSuccess() {
        User mockUser = Mockito.mock(User.class);
        String msg = "비트코인 1억 돌파!";

        Notification notification = new Notification(mockUser, msg);

        assertThat(notification.getUser()).isEqualTo(mockUser);
        assertThat(notification.getMessage()).isEqualTo(msg);
        assertThat(notification.isRead()).isFalse();
    }

    @Test
    @DisplayName("메시지가 비어있다면 예외가 발생한다")
    void blankMessageFail() {
        User mockUser = Mockito.mock(User.class);

        assertThatThrownBy(() -> new Notification(mockUser, ""))
                .isInstanceOf(PulserunException.class);
    }

    @Test
    @DisplayName("알림을 읽음 처리하면 isRead가 true가 된다")
    void markAsReadSuccess() {
        Notification notification = new Notification(Mockito.mock(User.class), "알림");

        notification.markAsRead();

        assertThat(notification.isRead()).isTrue();
    }
}