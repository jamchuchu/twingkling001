package com.sparta.twingkling001.login.securityLogin;

import com.sparta.twingkling001.api.exception.ErrorType;
import com.sparta.twingkling001.api.response.ApiResponse;
import com.sparta.twingkling001.api.response.SuccessType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/login")
@RestController
public class SecurityLoginController {

    @GetMapping("/success")
    public ResponseEntity<ApiResponse<?>> loginSuccess() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.LOGIN_SUCCESS));
    }
    @GetMapping("/permit/fail")
    public ResponseEntity<ApiResponse<?>> loginfail(){
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(ErrorType.LOGIN_FAIL));
    }
    @GetMapping("/permit/logout")
    public ResponseEntity<ApiResponse<?>> logoutSuccess(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.LOGOUT_SUCCESS));
    }

}
