package com.pulserun.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 공통
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", " 올바르지 않은 입력값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C002", " 허용되지 않은 메서드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C003", " 서버 내부 오류입니다."),
    DATA_IS_NULL(HttpStatus.INTERNAL_SERVER_ERROR, "C004", "{0} 값이 null입니다."),
    DATA_IS_BLANK(HttpStatus.INTERNAL_SERVER_ERROR, "C005", "{0} 값이 비어있습니다."),

    // User 도메인
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", " 존재하지 않는 사용자입니다."),

    // Rule 도메인
    RULE_NOT_FOUND(HttpStatus.NOT_FOUND, "R001", " 해당 규칙을 찾을 수 없습니다."),
    INVALID_CONDITION_TYPE(HttpStatus.BAD_REQUEST, "R002", " 잘못된 조건 타입입니다."),
    MISSING_SYMBOL(HttpStatus.BAD_REQUEST, "R003", " 심볼(코인명)은 필수 입력값입니다."),
    MISSING_INDICATOR_TYPE(HttpStatus.BAD_REQUEST, "R004", " 지표 타입은 필수 입력값입니다."),
    MISSING_TARGET_VALUE(HttpStatus.BAD_REQUEST, "R005", " 목표가는 필수 입력값입니다."),
    TARGET_VALUE_MUST_BE_POSITIVE(HttpStatus.BAD_REQUEST, "R006", " 목표가는 0보다 커야 합니다."),
    MISSING_CONDITION_TYPE(HttpStatus.BAD_REQUEST, "R007", " 조건 타입은 필수 입력값입니다."),
    CURRENT_PRICE_REQUIRED(HttpStatus.BAD_REQUEST, "R008", " 현재가 데이터가 필요합니다."),

    // Market 도메인
    MARKET_API_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "M001", " 거래소 API 연결에 실패했습니다."),
    MARKET_NOT_FOUND(HttpStatus.SERVICE_UNAVAILABLE, "M002", " 마켓이 존재하지 않습니다."),

    // Notification 도메인
    NOTIFICATION_API_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "N001", " 알람 API 연결에 실패했습니다."),
    NOTIFICATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "N002", " 존재하지 않는 알람입니다."),
    NOTIFICATION_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "N003", " 알람 열람 권한이 없습니다."),
    TARGET_VALUE_OUT_OF_RANGE(HttpStatus.INTERNAL_SERVER_ERROR, "N004", "목표 값이 범위를 벗어납니다."),

    // Wishlist 도메인
    WISHLIST_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "WL001", " 이미 즐겨찾기 되어있습니다."),
    WISHLIST_NOT_FOUND(HttpStatus.BAD_REQUEST, "WL001", " 즐겨찾기가 존재하지 않습니다."),

    // Jwt
    TOKEN_PARSE_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "J001"," 토큰 파싱에 실패했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}