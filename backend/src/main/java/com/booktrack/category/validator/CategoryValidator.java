package com.booktrack.category.validator;

import com.booktrack.category.entity.Category;
import com.booktrack.category.repository.CategoryRepository;
import com.booktrack.exception.DuplicateResourceException;
import com.booktrack.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import com.booktrack.category.validator.CategoryValidator;
import java.util.Objects;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryValidator {

    private final CategoryRepository categoryRepository;

    public Category validateCategoryExists(Long id) {

        return categoryRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found with id: " + id
                        ));
    }

    public void validateDuplicateName(String name) {

        String trimmedName = name.trim();

        if (categoryRepository.existsByNameIgnoreCase(trimmedName)) {

            throw new DuplicateResourceException(
                    "Category already exists with name: " + trimmedName
            );

        }

    }

    public void validateDuplicateNameForUpdate(
            Long categoryId,
            String name) {

        String trimmedName = name.trim();

        categoryRepository.findByNameIgnoreCase(trimmedName)
                .ifPresent(existingCategory -> {

                    if (!existingCategory.getId().equals(categoryId)) {

                        throw new DuplicateResourceException(
                                "Category already exists with name: "
                                        + trimmedName
                        );

                    }

                });

    }

}