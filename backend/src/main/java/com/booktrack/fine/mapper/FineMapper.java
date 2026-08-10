package com.booktrack.fine.mapper;

import com.booktrack.fine.dto.response.FineResponse;
import com.booktrack.fine.entity.Fine;
import org.springframework.stereotype.Component;

@Component
public class FineMapper {

    public FineResponse toResponse(Fine fine) {

        return FineResponse.builder()
                .id(fine.getId())

                .circulationId(
                        fine.getCirculation().getId())

                .bookCopyId(
                        fine.getCirculation()
                                .getBookCopy()
                                .getId())

                .barcode(
                        fine.getCirculation()
                                .getBookCopy()
                                .getBarcode())

                .bookId(
                        fine.getCirculation()
                                .getBookCopy()
                                .getBook()
                                .getId())

                .bookTitle(
                        fine.getCirculation()
                                .getBookCopy()
                                .getBook()
                                .getTitle())

                .isbn(
                        fine.getCirculation()
                                .getBookCopy()
                                .getBook()
                                .getIsbn())

                .userId(
                        fine.getCirculation()
                                .getUser()
                                .getId())

                .userName(
                        fine.getCirculation()
                                .getUser()
                                .getFirstName()
                                + " "
                                + fine.getCirculation()
                                        .getUser()
                                        .getLastName())

                .userEmail(
                        fine.getCirculation()
                                .getUser()
                                .getEmail())

                .amount(fine.getAmount())
                .reason(fine.getReason())
                .status(fine.getStatus())
                .paidAt(fine.getPaidAt())
                .notes(fine.getNotes())

                .createdAt(fine.getCreatedAt())
                .updatedAt(fine.getUpdatedAt())

                .build();
    }
}