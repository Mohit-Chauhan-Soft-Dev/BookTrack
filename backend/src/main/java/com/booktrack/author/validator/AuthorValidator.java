package com.booktrack.author.validator;

import com.booktrack.author.entity.Author;
import com.booktrack.author.repository.AuthorRepository;
import com.booktrack.exception.DuplicateResourceException;
import com.booktrack.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.Objects;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorValidator {

    private final AuthorRepository authorRepository;

    public Author validateAuthorExists(Long id) {

        return authorRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Author not found with id: " + id
                        ));
    }

    public void validateDuplicateAuthor(
            String firstName,
            String lastName) {

        String first = firstName.trim();
        String last = lastName.trim();

        if (authorRepository.existsByFirstNameIgnoreCaseAndLastNameIgnoreCase(first, last)) {

            throw new DuplicateResourceException(
                    "Author already exists: " + first + " " + last
            );
        }
    }

    public void validateDuplicateAuthorForUpdate(
            Long authorId,
            String firstName,
            String lastName) {

        String first = firstName.trim();
        String last = lastName.trim();

        authorRepository
                .findByFirstNameIgnoreCaseAndLastNameIgnoreCase(first, last)
                .ifPresent(existingAuthor -> {

                    if (!existingAuthor.getId().equals(authorId)) {

                        throw new DuplicateResourceException(
                                "Author already exists: " + first + " " + last
                        );

                    }

                });

    }

}