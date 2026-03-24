package com.pulserun.global.error.exception;

import com.pulserun.global.error.ErrorCode;
import lombok.Getter;

import java.text.MessageFormat;

@Getter
public class PulserunException extends RuntimeException {

    private final ErrorCode errorCode;

    public PulserunException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public PulserunException(ErrorCode errorCode, Object[] args) {
        super(MessageFormat.format(errorCode.getMessage(), args));
        this.errorCode = errorCode;
    }

    public PulserunException(ErrorCode errorCode, Exception e) {
        super(errorCode.getMessage(), e);
        this.errorCode = errorCode;
    }
}