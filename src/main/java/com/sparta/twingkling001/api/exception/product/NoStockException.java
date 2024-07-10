package com.sparta.twingkling001.api.exception.product;

public class NoStockException extends Exception{
    public NoStockException() {
        super("남은 재고 수량이 없습니다");
    }

    public NoStockException(String message) {
        super(message);
    }
}
