package com.booktrack.category.controller;

import com.booktrack.category.dto.request.CreateCategoryRequest;
import com.booktrack.category.dto.request.UpdateCategoryRequest;
import com.booktrack.category.dto.response.CategoryResponse;
import com.booktrack.category.service.CategoryService;
import com.booktrack.common.constants.ApplicationConstants;
import com.booktrack.common.dto.response.PageResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping(ApplicationConstants.CATEGORIES)
@RequiredArgsConstructor
public class CategoryController {

        private final CategoryService categoryService;

        @PostMapping
        @PreAuthorize("""
                        hasAnyAuthority(
                                'ROLE_LIBRARIAN',
                                'ROLE_ADMIN',
                                'ROLE_SUPER_ADMIN'
                        )
                        """)
        public ResponseEntity<CategoryResponse> createCategory(
                        @Valid @RequestBody CreateCategoryRequest request) {

                CategoryResponse response = categoryService.createCategory(request);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(response);
        }

        @GetMapping("/{id}")
        public ResponseEntity<CategoryResponse> getCategoryById(
                        @PathVariable Long id) {

                return ResponseEntity.ok(
                                categoryService.getCategoryById(id));
        }

        @GetMapping
        public ResponseEntity<PageResponse<CategoryResponse>> getAllCategories(

                        @RequestParam(defaultValue = "0") int page,

                        @RequestParam(defaultValue = "10") int size,

                        @RequestParam(defaultValue = "name") String sortBy,

                        @RequestParam(defaultValue = "asc") String sortDirection) {

                return ResponseEntity.ok(
                                categoryService.getAllCategories(
                                                page,
                                                size,
                                                sortBy,
                                                sortDirection));
        }

        @PutMapping("/{id}")
        @PreAuthorize("""
                        hasAnyAuthority(
                                'ROLE_LIBRARIAN',
                                'ROLE_ADMIN',
                                'ROLE_SUPER_ADMIN'
                        )
                        """)
        public ResponseEntity<CategoryResponse> updateCategory(

                        @PathVariable Long id,

                        @Valid @RequestBody UpdateCategoryRequest request) {

                return ResponseEntity.ok(
                                categoryService.updateCategory(id, request));
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("""
                        hasAnyAuthority(
                                'ROLE_ADMIN',
                                'ROLE_SUPER_ADMIN'
                        )
                        """)
        public ResponseEntity<Void> deleteCategory(
                        @PathVariable Long id) {

                categoryService.deleteCategory(id);

                return ResponseEntity.noContent().build();
        }

}