package com.sparta.twingkling001.api.exception.order;

public class InvalidReturnStatusException extends Exception{
    public InvalidReturnStatusException() {
        super("반품 가능 상태가 아닙니다");
    }

    public InvalidReturnStatusException(String message) {
        super(message);
    }
}
