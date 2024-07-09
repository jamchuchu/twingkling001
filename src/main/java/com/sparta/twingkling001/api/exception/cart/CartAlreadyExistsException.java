package com.sparta.twingkling001.api.exception.cart;

public class CartAlreadyExistsException extends Exception{
    public CartAlreadyExistsException() {
        super("이미 존재하는 회원 카트입니다");
    }

    public CartAlreadyExistsException(String message) {
        super(message);
    }
}
