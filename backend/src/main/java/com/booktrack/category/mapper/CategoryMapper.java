package com.booktrack.category.mapper;

import com.booktrack.category.dto.request.CreateCategoryRequest;
import com.booktrack.category.dto.request.UpdateCategoryRequest;
import com.booktrack.category.dto.response.CategoryResponse;
import com.booktrack.category.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CreateCategoryRequest request) {

        return Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    public CategoryResponse toResponse(Category category) {

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .active(category.isActive())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }

    public void updateEntity(
            Category category,
            UpdateCategoryRequest request) {

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setActive(request.isActive());
    }

}