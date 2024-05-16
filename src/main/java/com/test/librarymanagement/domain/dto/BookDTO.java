package com.test.librarymanagement.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class BookDTO {

    private Long id;

    private String title;

    private String author;

    private String publicationYear;

    private String ISBN;

    private Integer edition;
}
