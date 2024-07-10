package com.sparta.twingkling001.api.exception.general;

public class DataNotFoundException extends Exception{
    public DataNotFoundException() {
        super("해당 데이터가 존재하지 않습니다");
    }

    public DataNotFoundException(String message) {
        super(message);
    }
}
