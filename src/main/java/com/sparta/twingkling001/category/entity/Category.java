package com.sparta.twingkling001.category.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    private Long upperCategoryId;
    private String categoryName;

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public static Category from(Long upperCategoryId , String categoryName) {
        return Category.builder()
                .upperCategoryId(upperCategoryId)
                .categoryName(categoryName)
                .build();
    }
}
