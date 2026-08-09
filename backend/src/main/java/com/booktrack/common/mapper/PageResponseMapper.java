package com.booktrack.common.mapper;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.booktrack.common.dto.response.PageResponse;

import java.util.function.Function;

@Component
public class PageResponseMapper {

    public <E, R> PageResponse<R> toPageResponse(
            Page<E> page,
            Function<E, R> mapper) {

        return PageResponse.<R>builder()
                .content(
                        page.getContent()
                                .stream()
                                .map(mapper)
                                .toList()
                )
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .build();
    }

}