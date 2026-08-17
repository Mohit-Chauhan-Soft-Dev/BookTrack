package com.booktrack.author.controller;

import com.booktrack.author.dto.request.CreateAuthorRequest;
import com.booktrack.author.dto.request.UpdateAuthorRequest;
import com.booktrack.author.dto.response.AuthorResponse;
import com.booktrack.author.service.AuthorService;
import com.booktrack.common.constants.ApplicationConstants;
import com.booktrack.common.dto.response.PageResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping(ApplicationConstants.AUTHORS)
@RequiredArgsConstructor
public class AuthorController {

        private final AuthorService authorService;

        @PostMapping
        @PreAuthorize("""
                        hasAnyAuthority(
                                'ROLE_LIBRARIAN',
                                'ROLE_ADMIN',
                                'ROLE_SUPER_ADMIN'
                        )
                        """)
        public ResponseEntity<AuthorResponse> createAuthor(
                        @Valid @RequestBody CreateAuthorRequest request) {

                AuthorResponse response = authorService.createAuthor(request);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(response);
        }

        @GetMapping("/{id}")
        public ResponseEntity<AuthorResponse> getAuthorById(
                        @PathVariable Long id) {

                return ResponseEntity.ok(
                                authorService.getAuthorById(id));
        }

        @GetMapping
        public ResponseEntity<PageResponse<AuthorResponse>> getAllAuthors(

                        @RequestParam(defaultValue = "0") int page,

                        @RequestParam(defaultValue = "10") int size,

                        @RequestParam(defaultValue = "firstName") String sortBy,

                        @RequestParam(defaultValue = "asc") String sortDirection) {

                return ResponseEntity.ok(
                                authorService.getAllAuthors(
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
        public ResponseEntity<AuthorResponse> updateAuthor(

                        @PathVariable Long id,

                        @Valid @RequestBody UpdateAuthorRequest request) {

                return ResponseEntity.ok(
                                authorService.updateAuthor(id, request));
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("""
                        hasAnyAuthority(
                                'ROLE_ADMIN',
                                'ROLE_SUPER_ADMIN'
                        )
                        """)
        public ResponseEntity<Void> deleteAuthor(
                        @PathVariable Long id) {

                authorService.deleteAuthor(id);

                return ResponseEntity.noContent().build();
        }

}