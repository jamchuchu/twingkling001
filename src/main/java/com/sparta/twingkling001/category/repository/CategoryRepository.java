package com.sparta.twingkling001.category.repository;

import com.sparta.twingkling001.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findCategoryByCategoryId(Long categoryId);
    List<Category> findCategoriesByUpperCategoryId(Long categoryId);
    Category findCategoryByCategoryName(String categoryName);
    void deleteCategoryByCategoryId(Long categoryId);
}
