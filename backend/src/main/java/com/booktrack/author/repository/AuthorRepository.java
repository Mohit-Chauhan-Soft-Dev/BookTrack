package com.booktrack.author.repository;

import com.booktrack.author.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AuthorRepository extends
        JpaRepository<Author, Long>,
        JpaSpecificationExecutor<Author> {

    Optional<Author> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(
            String firstName,
            String lastName
    );

    boolean existsByFirstNameIgnoreCaseAndLastNameIgnoreCase(
            String firstName,
            String lastName
    );

}