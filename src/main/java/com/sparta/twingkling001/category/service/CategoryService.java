package com.sparta.twingkling001.category.service;

import com.sparta.twingkling001.api.exception.general.DataNotFoundException;
import com.sparta.twingkling001.category.dto.response.CategoryRespDto;
import com.sparta.twingkling001.category.entity.Category;
import com.sparta.twingkling001.category.repository.CategoryRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final EntityManager entityManager;


    public CategoryRespDto addCategory(Long upperCategoryId, String categoryName) {
        Category category = categoryRepository.save(Category.from(upperCategoryId, categoryName));
        return CategoryRespDto.from(category);
    }

    public List<CategoryRespDto> getLowerCategories(Long categoryId) {
        List<Category> categories = categoryRepository.findCategoriesByUpperCategoryId(categoryId);
        return categories.stream().map(CategoryRespDto::from).toList();
    }

    public List<CategoryRespDto> getUpperCategories(Long categoryId) {
        ArrayDeque<Category> categories = new ArrayDeque<>();
        //첫번째 것 추가
        categories.add(categoryRepository.findCategoryByCategoryId(categoryId));
        while(true) {
            //상위 카테고리아이디로 찾아서 추가
            Long upperCategoryId = categories.peekFirst().getUpperCategoryId();
            if (upperCategoryId == 0) { // 최상단 카테고리
                break;
            }
            categories.addFirst(categoryRepository.findCategoryByCategoryId(upperCategoryId));
        }
        return categories.stream().map(CategoryRespDto::from).toList();
    }

    public CategoryRespDto getCategoryByCategoryName(String categoryName) {
        return CategoryRespDto.from(categoryRepository.findCategoryByCategoryName(categoryName));
    }

    public CategoryRespDto getCategoryByCategoryId(Long categoryId) {
        return CategoryRespDto.from(categoryRepository.findCategoryByCategoryId(categoryId));
    }

    public void updateCategoryName(Long categoryId, String categoryName) throws DataNotFoundException {
        Category category = entityManager.find(Category.class, categoryId);
        if(category == null){
            throw new DataNotFoundException();
        }
        category.setCategoryName(categoryName);

    }

    public void deleteCategoryName(Long categoryId) {
        categoryRepository.deleteCategoryByCategoryId(categoryId);
    }

    public void deleteCategoryAndLowerCategories(Long categoryId) {
        ArrayDeque<Long> categoriesNum = new ArrayDeque<>();
        categoriesNum.add(categoryId);
        while(!categoriesNum.isEmpty()){
            long lowerCategoryId = categoriesNum.poll();
            categoryRepository.deleteCategoryByCategoryId(lowerCategoryId); // 삭제
            categoriesNum.addAll(categoryRepository.findCategoriesByUpperCategoryId(lowerCategoryId).stream()
                    .map(Category::getCategoryId).toList()); //해당 num을 상위 카테고리를 가진 category들: 하위 카테고리들
        }
    }
}
