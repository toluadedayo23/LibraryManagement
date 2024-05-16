package com.test.librarymanagement.domain.input.book;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class BookUpdateInput {

    private String title;

    private String author;

    private String publicationYear;

    private String ISBN;

    private Integer edition;
}
