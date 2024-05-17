package com.test.librarymanagement.mapper;

import com.test.librarymanagement.domain.data.Book;
import com.test.librarymanagement.domain.dto.BookDTO;
import com.test.librarymanagement.domain.dto.PageableDTO;
import com.test.librarymanagement.domain.input.book.BookCreateInput;
import com.test.librarymanagement.domain.input.book.BookUpdateInput;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface BookMapper {

    Book mapInputToBook(BookCreateInput input);

    BookDTO mapBookToDTO(Book book);

    @Mapping(target = "pageNumber", source = "number")
    PageableDTO<BookDTO> mapToPageableDTO(Page<Book> result);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void bookUpdateInputToBook(BookUpdateInput updateInput, @MappingTarget Book book);

    BookCreateInput mapBookToCreateInput(Book book);
}
