package com.test.librarymanagement.domain.dto;

import com.test.librarymanagement.domain.enums.BookStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@NoArgsConstructor
public class BookDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String author;

    private String publicationYear;

    private String ISBN;

    private Integer edition;

    private BookStatus bookStatus;
}
