package com.sparta.twingkling001.api.exception.product;

public class UnExpectedLockException extends Exception{
    public UnExpectedLockException() {
        super("예상치 못한 에러가 발생하였습니다");
    }

    public UnExpectedLockException(String message) {
        super(message);
    }
}
