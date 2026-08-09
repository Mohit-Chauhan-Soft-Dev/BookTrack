package com.booktrack.category.service;

import com.booktrack.category.dto.request.CreateCategoryRequest;
import com.booktrack.category.dto.request.UpdateCategoryRequest;
import com.booktrack.category.dto.response.CategoryResponse;
import com.booktrack.common.dto.response.PageResponse;

public interface CategoryService {

    CategoryResponse createCategory(CreateCategoryRequest request);

    CategoryResponse updateCategory(Long id,
                                    UpdateCategoryRequest request);

    void deleteCategory(Long id);

    CategoryResponse getCategoryById(Long id);

    PageResponse<CategoryResponse> getAllCategories(
        int page,
        int size,
        String sortBy,
        String sortDirection
);

}