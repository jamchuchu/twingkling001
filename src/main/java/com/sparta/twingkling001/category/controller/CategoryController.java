package com.sparta.twingkling001.category.controller;


import com.sparta.twingkling001.api.response.ApiResponse;
import com.sparta.twingkling001.api.response.SuccessType;
import com.sparta.twingkling001.category.dto.response.CategoryRespDto;
import com.sparta.twingkling001.category.entity.Category;
import com.sparta.twingkling001.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/categories")
@RestController
public class CategoryController {
    private final CategoryService categoryService;

    //첫 입력시 upper 0
    @PostMapping("/")
    public ResponseEntity<ApiResponse<CategoryRespDto>> addCategory (@RequestParam Long upperCategoryId, String categoryName){
        CategoryRespDto response = categoryService.addCategory(upperCategoryId, categoryName);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(SuccessType.SUCCESS_CREATE, response));
    }

    // 카테고리 별 하위 카테고리 조회
    @GetMapping("/{categoryId}/lower")
    public ResponseEntity<ApiResponse<List<CategoryRespDto>>> getLowerCategories(@PathVariable Long categoryId) {
        List<CategoryRespDto> response = categoryService.getLowerCategories(categoryId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS, response));
    }

    // 해당 카테고리의 상위 단계 모두 조회 (바지 -> 반바지 -> 청반바지)
    @GetMapping("/{categoryId}/upper")
    public ResponseEntity<ApiResponse<List<CategoryRespDto>>> getUpperCategories(@PathVariable Long categoryId) {
        List<CategoryRespDto> response = categoryService.getUpperCategories(categoryId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS, response));
    }

    // 카테고리 이름으로 조회
    @GetMapping("/name")
    public ResponseEntity<ApiResponse<CategoryRespDto>> getCategory(@RequestParam String categoryName) {
        CategoryRespDto response = categoryService.getCategoryByCategoryName(categoryName);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS, response));
    }

    // 카테고리 id로 조회
    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<CategoryRespDto>> getCategory(@PathVariable Long categoryId) {
        CategoryRespDto response = categoryService.getCategoryByCategoryId(categoryId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS, response));
    }

    // 카테고리 이름 수정
    @PatchMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<?>> updateCategoryName (@PathVariable Long categoryId, @RequestParam String categoryName){
        categoryService.updateCategoryName(categoryId, categoryName);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }

    // 해당 카테고리 삭제
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<?>> deleteCategory (@PathVariable Long categoryId){
        categoryService.deleteCategoryName(categoryId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }


    // 해당 카테고리의 하위 카테고리 모두 삭제 (반바지 삭제 -> 청반바지 백반바지 노랑반바지....모두 삭제)
    @DeleteMapping("/{categoryId}/lower")
    public ResponseEntity<ApiResponse<?>> deleteCategoryAndLowerCategories (@PathVariable Long categoryId){
        categoryService.deleteCategoryAndLowerCategories(categoryId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(SuccessType.SUCCESS));
    }




}
