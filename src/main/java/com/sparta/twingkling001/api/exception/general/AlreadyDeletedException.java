package com.sparta.twingkling001.api.exception.general;

public class AlreadyDeletedException extends Exception{
    public AlreadyDeletedException() {
        super("이미 삭제된 데이터 입니다");
    }

    public AlreadyDeletedException(String message) {
        super(message);
    }
}
