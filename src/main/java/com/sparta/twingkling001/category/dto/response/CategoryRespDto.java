package com.sparta.twingkling001.category.dto.response;

import com.sparta.twingkling001.category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CategoryRespDto {
    private Long categoryId;
    private Long upperCategoryId;
    private String categoryName;

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public static CategoryRespDto from(Category category){
        return CategoryRespDto.builder()
                .categoryId(category.getCategoryId())
                .upperCategoryId(category.getUpperCategoryId())
                .categoryName(category.getCategoryName())
                .build();
    }
}
