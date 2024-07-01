package com.sparta.twingkling001.api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {


    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰 불일치"),
    INVALID_EMAIL_FORM(HttpStatus.UNAUTHORIZED, "이메일 형식이 올바르지 않습니다"),
    INVALID_EMAIL_DUPLE(HttpStatus.UNAUTHORIZED, "이메일이 중복 되었습니다"),
    LOGIN_FAIL(HttpStatus.UNAUTHORIZED, "로그인 실패"),
    NOT_FOUND_MEMBER(HttpStatus.UNAUTHORIZED, "사용자를 찾을 수 없습니다"),
    NOT_FOUND_STOCK(HttpStatus.NOT_FOUND, "재고가 없습니다"),
    NOT_CANSEL(HttpStatus.BAD_REQUEST, "배송중에는 주문을 취소 할 수 없습니다");


    private final HttpStatus httpStatus;
    private final String message;
}
