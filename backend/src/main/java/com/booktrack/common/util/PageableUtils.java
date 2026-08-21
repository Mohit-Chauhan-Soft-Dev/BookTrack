package com.booktrack.common.util;

import com.booktrack.exception.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PageableUtils {

    private static final int MAX_PAGE_SIZE = 100;

    private PageableUtils() {
    }

    public static Pageable createPageable(
            int page,
            int size,
            String sortBy,
            String sortDirection) {

        if (page < 0) {
            throw new BadRequestException(
                    "Page index cannot be negative.");
        }

        if (size <= 0) {
            throw new BadRequestException(
                    "Page size must be greater than zero.");
        }

        if (size > MAX_PAGE_SIZE) {
            throw new BadRequestException(
                    "Page size cannot exceed " + MAX_PAGE_SIZE + ".");
        }

        if (sortBy == null || sortBy.isBlank()) {
            throw new BadRequestException(
                    "Sort field is required.");
        }

        Sort.Direction direction =
                Sort.Direction
                        .fromOptionalString(sortDirection)
                        .orElse(Sort.Direction.ASC);

        return PageRequest.of(
                page,
                size,
                Sort.by(direction, sortBy));
    }
}