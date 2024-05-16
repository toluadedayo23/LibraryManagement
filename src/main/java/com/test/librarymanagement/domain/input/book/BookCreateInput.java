package com.test.librarymanagement.domain.input.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class BookCreateInput {

    @NotNull
    @Size(min = 2, max = 30, message = "Title cannot be less than 2 or more than 30 characters")
    private String title;

    @NotNull
    @Size(min = 3, max = 20, message = "Author cannot be less than 2 or more than 20 characters")
    private String author;

    @NotNull
    @Size(min = 4, max = 4, message = "Publication year cannot be less than 4 or more than 4 characters")
    private String publicationYear;

    @NotNull
    @Size(min = 4, max = 20, message = "ISBN cannot be less than 4 or more than 20 characters")
    private String ISBN;

    @NotNull
    @Min(value = 1)
    private Integer edition;
}
