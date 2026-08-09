package com.booktrack.common.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PageableUtils {

    private PageableUtils() {
    }

    public static Pageable createPageable(
            int page,
            int size,
            String sortBy,
            String sortDirection) {

        Sort.Direction direction =
                Sort.Direction
                        .fromOptionalString(sortDirection)
                        .orElse(Sort.Direction.ASC);

        return PageRequest.of(
                page,
                size,
                Sort.by(direction, sortBy)
        );
    }

}