package com.sparta.twingkling001.product.controller;

import com.sparta.twingkling001.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

//    @GetMapping()
//    public ResponseEntity<ApiResponse<Role>> getRole() {
//        Role response = roleService.getRole(1);
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .body(ApiResponse.success(SuccessType.SUCCESS, response));
//    }

}
