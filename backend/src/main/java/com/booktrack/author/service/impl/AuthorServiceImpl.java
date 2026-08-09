package com.booktrack.author.service.impl;

import com.booktrack.author.dto.request.CreateAuthorRequest;
import com.booktrack.author.dto.request.UpdateAuthorRequest;
import com.booktrack.author.dto.response.AuthorResponse;
import com.booktrack.author.entity.Author;
import com.booktrack.author.mapper.AuthorMapper;
import com.booktrack.author.repository.AuthorRepository;
import com.booktrack.author.service.AuthorService;
import com.booktrack.author.validator.AuthorValidator;
import com.booktrack.common.dto.response.PageResponse;
import com.booktrack.common.mapper.PageResponseMapper;
import com.booktrack.common.util.PageableUtils;
import com.booktrack.exception.BadRequestException;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthorServiceImpl implements AuthorService {

        private final AuthorRepository authorRepository;

        private final AuthorMapper authorMapper;

        private final AuthorValidator authorValidator;

        private final PageResponseMapper pageResponseMapper;

        @Override
        public AuthorResponse createAuthor(CreateAuthorRequest request) {

                String firstName = request.getFirstName().trim();
                String lastName = request.getLastName().trim();

                authorValidator.validateDuplicateAuthor(
                                firstName,
                                lastName);

                request.setFirstName(firstName);
                request.setLastName(lastName);

                Author author = authorMapper.toEntity(request);

                Author savedAuthor = authorRepository.save(author);

                return authorMapper.toResponse(savedAuthor);
        }

        @Override
        public AuthorResponse updateAuthor(
                        Long id,
                        UpdateAuthorRequest request) {

                Author author = authorValidator.validateAuthorExists(id);

                String firstName = request.getFirstName().trim();

                String lastName = request.getLastName().trim();

                authorValidator.validateDuplicateAuthorForUpdate(
                                id,
                                firstName,
                                lastName);

                request.setFirstName(firstName);
                request.setLastName(lastName);

                authorMapper.updateEntity(author, request);

                Author updatedAuthor = authorRepository.save(author);

                return authorMapper.toResponse(updatedAuthor);
        }

        @Override
        public void deleteAuthor(Long id) {

                Author author = authorValidator.validateAuthorExists(id);

                if (!author.isActive()) {

                        throw new BadRequestException("Author is already inactive.");

                }

                author.setActive(false);

                authorRepository.save(author);
        }

        @Override
        @Transactional(readOnly = true)
        public AuthorResponse getAuthorById(Long id) {

                Author author = authorValidator.validateAuthorExists(id);

                return authorMapper.toResponse(author);
        }

        @Override
        @Transactional(readOnly = true)
        public PageResponse<AuthorResponse> getAllAuthors(
                        int page,
                        int size,
                        String sortBy,
                        String sortDirection) {

                Pageable pageable = PageableUtils.createPageable(
                                page,
                                size,
                                sortBy,
                                sortDirection);

                Page<Author> authorPage = authorRepository.findAll(pageable);

                return pageResponseMapper.toPageResponse(
                                authorPage,
                                authorMapper::toResponse);
        }
}