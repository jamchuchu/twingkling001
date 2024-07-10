package com.sparta.twingkling001.api.exception.order;

public class ReturnPeriodExpiredException extends Exception{
    public ReturnPeriodExpiredException() {
        super("반품 가능 기간이 지났습니다");
    }

    public ReturnPeriodExpiredException(String message) {
        super(message);
    }
}
