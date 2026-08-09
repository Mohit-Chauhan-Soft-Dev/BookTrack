package com.booktrack.author.mapper;

import com.booktrack.author.dto.request.CreateAuthorRequest;
import com.booktrack.author.dto.request.UpdateAuthorRequest;
import com.booktrack.author.dto.response.AuthorResponse;
import com.booktrack.author.entity.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

    public Author toEntity(CreateAuthorRequest request) {

        return Author.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .biography(request.getBiography())
                .build();
    }

    public AuthorResponse toResponse(Author author) {

        return AuthorResponse.builder()
                .id(author.getId())
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .biography(author.getBiography())
                .active(author.isActive())
                .createdAt(author.getCreatedAt())
                .updatedAt(author.getUpdatedAt())
                .build();
    }

    public void updateEntity(
            Author author,
            UpdateAuthorRequest request) {

        author.setFirstName(request.getFirstName());
        author.setLastName(request.getLastName());
        author.setBiography(request.getBiography());
        author.setActive(request.isActive());
    }

}