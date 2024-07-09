package com.sparta.twingkling001.category.service;

import com.sparta.twingkling001.api.exception.general.DataNotFoundException;
import com.sparta.twingkling001.category.dto.response.CategoryRespDto;
import com.sparta.twingkling001.category.entity.Category;
import com.sparta.twingkling001.category.repository.CategoryRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private CategoryService categoryService;

    @Test
    @DisplayName("카테고리 추가")
    void addCategory() {
        //given
        Long upperCategoryId = 1L;
        String categoryName = "카테고리1";
        Category category = new Category(1L, 0L, "카테고리1");
        when(categoryRepository.save(any(Category.class))).thenReturn(category);



        //when
        CategoryRespDto expected = CategoryRespDto.from(category);
        CategoryRespDto response = categoryService.addCategory(upperCategoryId, categoryName);

        //then
        assertEquals(expected.getClass() , response.getClass());
        assertEquals(expected.getCategoryId() , response.getCategoryId());
        assertEquals(expected.getUpperCategoryId() , response.getUpperCategoryId());
        assertEquals(expected.getCategoryName() , response.getCategoryName());
    }

    @Test
    @DisplayName("하위 카테고리 조회")
    void getLowerCategories() {
        //given
        Long categoryId = 1L;
        List<Category> categories = new ArrayList<>();
        categories.add(new Category(2L, 1L, "카테고리2"));
        categories.add(new Category(3L, 1L, "카테고리3"));
        categories.add(new Category(4L, 1L, "카테고리4"));

        //when
        List<CategoryRespDto> expected = new ArrayList<>();
        when(categoryRepository.findCategoriesByUpperCategoryId(categoryId)).thenReturn(categories);
        for(Category c :  categories){
            expected.add(CategoryRespDto.from(c));
        }
        List<CategoryRespDto> response = categoryService.getLowerCategories(categoryId);

        //then
        assertEquals(expected.size(), response.size());
        assertEquals(expected.get(0).getCategoryName(), response.get(0).getCategoryName());
        assertEquals(expected.get(1).getCategoryName(), response.get(1).getCategoryName());
        assertEquals(expected.get(2).getCategoryName(), response.get(2).getCategoryName());
    }

    @Test
    @DisplayName("아래 카테고리 리스트 들고오기 -- 최상단")
    void getUpperCategories() {
        //given
        Long categoryId = 1L;
        Category category = new Category(1L, 0L, "카테고리1");

        //when
        when(categoryRepository.findCategoryByCategoryId(categoryId)).thenReturn(category);
        List<CategoryRespDto> expected = new ArrayList<>();
        expected.add(CategoryRespDto.from(category));
        List<CategoryRespDto> response = categoryService.getUpperCategories(categoryId);

        //then
        assertEquals(expected.size(), response.size());
        assertEquals(expected.get(0).getCategoryName(), response.get(0).getCategoryName());
    }

    @Test
    @DisplayName("카테고리 이름으로 카테고리 조회")
    void getCategoryByCategoryName() {
        //given
        String categoryName = "카테고리1";
        Category category = new Category(1L, 0L, "카테고리1");

        //when
        when(categoryRepository.findCategoryByCategoryName(categoryName)).thenReturn(category);
        CategoryRespDto expected = CategoryRespDto.from(category);
        CategoryRespDto response = categoryService.getCategoryByCategoryName(categoryName);

        //then
        assertEquals(expected.getCategoryId(), response.getCategoryId());
        assertEquals(expected.getUpperCategoryId(), response.getUpperCategoryId());
        assertEquals(expected.getCategoryName(), response.getCategoryName());
    }

    @Test
    @DisplayName("카테고리 번호로 카테고리 조회")
    void getCategoryByCategoryId() {
        //given
        Long categoryId = 1L;
        Category category = new Category(1L, 0L, "카테고리1");

        //when
        when(categoryRepository.findCategoryByCategoryId(categoryId)).thenReturn(category);
        CategoryRespDto expected = CategoryRespDto.from(category);
        CategoryRespDto response = categoryService.getCategoryByCategoryId(categoryId);

        //then
        assertEquals(expected.getCategoryId(), response.getCategoryId());
        assertEquals(expected.getUpperCategoryId(), response.getUpperCategoryId());
        assertEquals(expected.getCategoryName(), response.getCategoryName());
    }

    @Test
    @DisplayName("카테고리 이름 변경")
    void updateCategoryName() throws DataNotFoundException {
        //given
        Long categoryId = 1L;
        String categoryName = "새로운 카테고리1";
        Category category = new Category(1L, 0L, "카테고리1");

        //when
        when(entityManager.find(Category.class, categoryId)).thenReturn(category);
        categoryService.updateCategoryName(categoryId, categoryName);

        //then
        assertEquals(1L, category.getCategoryId());
        assertEquals(0L, category.getUpperCategoryId());
        assertNotEquals("카테고리1", category.getCategoryName());
        assertEquals("새로운 카테고리1", category.getCategoryName());
    }

    @Test
    @DisplayName("카테고리 이름 변경 -- 번호 오류")
    void updateCategoryNameNotFoundSuchData() throws Exception{
        // given
        Long categoryId = 1L;
        String categoryName = "새로운 카테고리1";
        Category category = null;

        // when
        when(entityManager.find(Category.class, categoryId)).thenReturn(category);

        // then
        assertThrows(DataNotFoundException.class, () -> {
            categoryService.updateCategoryName(categoryId, categoryName);
        });
    }

//    @Test
//    void deleteCategoryName() {
//    }
//
//    @Test
//    void deleteCategoryAndLowerCategories() {
//    }
}