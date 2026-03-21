package com.pulserun.global.error.exception;

import com.pulserun.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, Exception e) {
        super(errorCode.getMessage(), e);
        this.errorCode = errorCode;
    }
}