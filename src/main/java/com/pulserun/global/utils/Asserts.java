package com.pulserun.global.utils;

import com.pulserun.global.error.ErrorCode;
import com.pulserun.global.error.exception.PulserunException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Asserts {

    public static void notNull(Object object, ErrorCode errorCode) {
        if (object == null) {
            throw new PulserunException(errorCode);
        }
    }

    public static void notNull(Object object, ErrorCode errorCode, Object... args) {
        if (object == null) {
            throw new PulserunException(errorCode, args);
        }
    }

    public static void hasText(String text, ErrorCode errorCode, Object... args) {
        if (text == null || text.isBlank()) {
            throw new PulserunException(errorCode, args);
        }
    }
}
