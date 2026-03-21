package com.pulserun.global.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 공통
    INVALID_INPUT_VALUE(400, "C001", " 올바르지 않은 입력값입니다."),
    METHOD_NOT_ALLOWED(405, "C002", " 허용되지 않은 메서드입니다."),
    INTERNAL_SERVER_ERROR(500, "C003", " 서버 내부 오류입니다."),

    // Rule 도메인
    RULE_NOT_FOUND(404, "R001", " 해당 규칙을 찾을 수 없습니다."),
    INVALID_CONDITION_TYPE(400, "R002", " 잘못된 조건 타입(ABOVE/BELOW)입니다."),

    // Market 도메인
    MARKET_API_ERROR(503, "M001", " 거래소 API 연결에 실패했습니다."),

    // Jwt
    TOKEN_PARSE_ERROR(503, "J001", "토큰 파싱에 실패했습니다.");

    private final int status;
    private final String code;
    private final String message;
}