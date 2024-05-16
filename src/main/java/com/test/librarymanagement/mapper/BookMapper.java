package com.test.librarymanagement.mapper;

import com.test.librarymanagement.domain.data.Book;
import com.test.librarymanagement.domain.dto.BookDTO;
import com.test.librarymanagement.domain.input.book.BookCreateInput;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookMapper {

    Book mapInputToBook(BookCreateInput input);

    BookDTO mapBookToDTO(Book book);
}
