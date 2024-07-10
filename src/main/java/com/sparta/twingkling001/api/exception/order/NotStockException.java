package com.sparta.twingkling001.api.exception.order;

public class NotStockException extends Exception{
    public NotStockException() {
        super("남은 판매 가능 물품이 없습니다");
    }

    public NotStockException(String message) {
        super(message);
    }
}
