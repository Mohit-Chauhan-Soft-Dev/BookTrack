package com.booktrack.category.service.impl;

import com.booktrack.category.dto.request.CreateCategoryRequest;
import com.booktrack.category.dto.request.UpdateCategoryRequest;
import com.booktrack.category.dto.response.CategoryResponse;
import com.booktrack.category.entity.Category;
import com.booktrack.category.mapper.CategoryMapper;
import com.booktrack.category.repository.CategoryRepository;
import com.booktrack.category.service.CategoryService;
import com.booktrack.category.validator.CategoryValidator;
import com.booktrack.common.dto.response.PageResponse;
import com.booktrack.common.mapper.PageResponseMapper;
import com.booktrack.common.util.PageableUtils;
import com.booktrack.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    private final CategoryValidator categoryValidator;

    private final PageResponseMapper pageResponseMapper;

    @Override
    public CategoryResponse createCategory(
            CreateCategoryRequest request) {

        String name = request.getName().trim();

        categoryValidator.validateDuplicateName(name);

        request.setName(name);

        Category category =
                categoryMapper.toEntity(request);

        Category savedCategory =
                categoryRepository.save(category);

        return categoryMapper.toResponse(savedCategory);
    }

    @Override
    public CategoryResponse updateCategory(
            Long id,
            UpdateCategoryRequest request) {

        Category category =
                categoryValidator.validateCategoryExists(id);

        String name =
                request.getName().trim();

        categoryValidator.validateDuplicateNameForUpdate(
                id,
                name
        );

        request.setName(name);

        categoryMapper.updateEntity(
                category,
                request
        );

        Category updatedCategory =
                categoryRepository.save(category);

        return categoryMapper.toResponse(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {

        Category category =
                categoryValidator.validateCategoryExists(id);

        if (!category.isActive()) {

            throw new DuplicateResourceException(
                    "Category is already inactive."
            );
        }

        category.setActive(false);

        categoryRepository.save(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {

        Category category =
                categoryValidator.validateCategoryExists(id);

        return categoryMapper.toResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CategoryResponse> getAllCategories(
            int page,
            int size,
            String sortBy,
            String sortDirection) {

        Pageable pageable =
                PageableUtils.createPageable(
                        page,
                        size,
                        sortBy,
                        sortDirection
                );

        Page<Category> categoryPage =
                categoryRepository.findAll(pageable);

        return pageResponseMapper.toPageResponse(
                categoryPage,
                categoryMapper::toResponse
        );
    }
}