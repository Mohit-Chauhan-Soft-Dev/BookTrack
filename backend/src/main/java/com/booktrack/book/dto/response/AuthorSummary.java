package com.booktrack.book.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorSummary {

    private Long id;

    private String firstName;

    private String lastName;

}