package com.booktrack.book.mapper;

import com.booktrack.book.dto.request.CreateBookRequest;
import com.booktrack.book.dto.request.UpdateBookRequest;
import com.booktrack.book.dto.response.AuthorSummary;
import com.booktrack.book.dto.response.BookResponse;
import com.booktrack.book.entity.Book;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class BookMapper {

    public Book toEntity(CreateBookRequest request) {

        return Book.builder()
                .isbn(request.getIsbn())
                .title(request.getTitle())
                .subtitle(request.getSubtitle())
                .description(request.getDescription())
                .language(request.getLanguage())
                .edition(request.getEdition())
                .publicationYear(request.getPublicationYear())
                .totalPages(request.getTotalPages())
                .totalCopies(request.getTotalCopies())
                .availableCopies(request.getTotalCopies())
                .shelfLocation(request.getShelfLocation())
                .coverImage(request.getCoverImage())
                .price(request.getPrice())
                .build();

    }

    public void updateEntity(
            Book book,
            UpdateBookRequest request) {

        book.setIsbn(request.getIsbn());
        book.setTitle(request.getTitle());
        book.setSubtitle(request.getSubtitle());
        book.setDescription(request.getDescription());
        book.setLanguage(request.getLanguage());
        book.setEdition(request.getEdition());
        book.setPublicationYear(request.getPublicationYear());
        book.setTotalPages(request.getTotalPages());
        book.setTotalCopies(request.getTotalCopies());
        book.setAvailableCopies(request.getAvailableCopies());
        book.setShelfLocation(request.getShelfLocation());
        book.setCoverImage(request.getCoverImage());
        book.setPrice(request.getPrice());
        book.setStatus(request.getStatus());
        book.setActive(request.isActive());

    }

    public BookResponse toResponse(Book book) {

        return BookResponse.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .subtitle(book.getSubtitle())
                .description(book.getDescription())
                .language(book.getLanguage())
                .edition(book.getEdition())
                .publicationYear(book.getPublicationYear())
                .totalPages(book.getTotalPages())
                .totalCopies(book.getTotalCopies())
                .availableCopies(book.getAvailableCopies())
                .shelfLocation(book.getShelfLocation())
                .coverImage(book.getCoverImage())
                .price(book.getPrice())
                .active(book.isActive())
                .status(book.getStatus())

                .categoryId(book.getCategory().getId())
                .categoryName(book.getCategory().getName())

                .publisherId(book.getPublisher().getId())
                .publisherName(book.getPublisher().getName())

                .authors(
                        book.getAuthors()
                                .stream()
                                .map(author ->
                                        AuthorSummary.builder()
                                                .id(author.getId())
                                                .firstName(author.getFirstName())
                                                .lastName(author.getLastName())
                                                .build())
                                .collect(Collectors.toSet())
                )

                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();

    }

}