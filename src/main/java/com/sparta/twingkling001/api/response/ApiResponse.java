package com.sparta.twingkling001.api.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import com.sparta.twingkling001.api.exception.ErrorType;


@Getter
@JsonPropertyOrder({"code", "message", "data"})
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    private final int code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public static ApiResponse<?> success(com.sparta.twingkling.api.response.SuccessType successType) {
        return new ApiResponse<>(successType.getHttpStatusCode(), successType.getMessage());
    }

    public static <T> ApiResponse<T> success(com.sparta.twingkling.api.response.SuccessType successType, T data) {
        return new ApiResponse<T>(successType.getHttpStatusCode(), successType.getMessage(), data);
    }

    public static ApiResponse<?> error(ErrorType errorType) {
        return new ApiResponse<>(errorType.getHttpStatus().value(), errorType.getMessage(), errorType);
    }

    public static <T> ApiResponse<T> error(ErrorType errorType, T data) {
        return new ApiResponse<T>(errorType.getHttpStatus().value(), errorType.getMessage(), data);
    }
}