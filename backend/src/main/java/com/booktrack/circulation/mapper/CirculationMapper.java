package com.booktrack.circulation.mapper;

import com.booktrack.circulation.dto.response.CirculationResponse;
import com.booktrack.circulation.entity.Circulation;
import org.springframework.stereotype.Component;

@Component
public class CirculationMapper {

    public CirculationResponse toResponse(Circulation circulation) {

        return CirculationResponse.builder()
                .id(circulation.getId())

                .bookCopyId(
                        circulation.getBookCopy().getId()
                )
                .barcode(
                        circulation.getBookCopy().getBarcode()
                )

                .bookId(
                        circulation.getBookCopy()
                                .getBook()
                                .getId()
                )
                .bookTitle(
                        circulation.getBookCopy()
                                .getBook()
                                .getTitle()
                )
                .isbn(
                        circulation.getBookCopy()
                                .getBook()
                                .getIsbn()
                )

                .userId(
                        circulation.getUser().getId()
                )
                .userName(
                        circulation.getUser().getFirstName()
                                + " "
                                + circulation.getUser().getLastName()
                )
                .userEmail(
                        circulation.getUser().getEmail()
                )

                .issuedAt(circulation.getIssuedAt())
                .dueAt(circulation.getDueAt())
                .returnedAt(circulation.getReturnedAt())
                .status(circulation.getStatus())
                .renewalCount(circulation.getRenewalCount())
                .notes(circulation.getNotes())

                .createdAt(circulation.getCreatedAt())
                .updatedAt(circulation.getUpdatedAt())

                .build();
    }
}