package com.test.librarymanagement.service;

import com.test.librarymanagement.domain.data.Book;
import com.test.librarymanagement.domain.dto.BookDTO;
import com.test.librarymanagement.domain.dto.PageableDTO;
import com.test.librarymanagement.domain.enums.BookStatus;
import com.test.librarymanagement.domain.input.book.BookCreateInput;
import com.test.librarymanagement.domain.input.book.BookUpdateInput;
import com.test.librarymanagement.mapper.BookMapper;
import com.test.librarymanagement.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {
    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void should_throw_IllegalArgumentException_if_bookProperties_exist(){
        BookCreateInput input = BookCreateInput.builder()
                .title("title 1")
                .author("author 1")
                .publicationYear("2009")
                .ISBN("ISBN 0-061-96436-0")
                .edition(3)
                .build();

        when(bookRepository.existsByTitleAndAuthorAndPublicationYearAndISBNAndEdition(input.getTitle(), input.getAuthor(), input.getPublicationYear(),input.getISBN(), input.getEdition())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> bookService.addBook(input));

    }

    @Test
    public void should_add_book(){
        BookCreateInput input = BookCreateInput.builder()
                .title("title 1")
                .author("author 1")
                .publicationYear("2009")
                .ISBN("ISBN 0-061-96436-0")
                .edition(3).build();

        Book book = Book.builder()
                .id(1L)
                .title("title 1")
                .author("author 1")
                .publicationYear("2009")
                .ISBN("ISBN 0-061-96436-0")
                .edition(3)
                .bookStatus(BookStatus.AVAILABLE)
                .build();

        BookDTO bookDTO = BookDTO.builder()
                .id(1L)
                .title("title 1")
                .author("author 1")
                .publicationYear("2009")
                .ISBN("ISBN 0-061-96436-0")
                .edition(3)
                .bookStatus(BookStatus.AVAILABLE)
                .build();

        when(bookRepository.existsByTitleAndAuthorAndPublicationYearAndISBNAndEdition(input.getTitle(), input.getAuthor(), input.getPublicationYear(),input.getISBN(), input.getEdition())).thenReturn(false);
        when(bookMapper.mapInputToBook(input)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.mapBookToDTO(book)).thenReturn(bookDTO);

        BookDTO response = bookService.addBook(input);

        assertEquals(input.getISBN(), response.getISBN());
        assertEquals(input.getEdition(), response.getEdition());
    }


    @Test
    public void should_get_book(){

        Long bookId = 1L;

        Book book = Book.builder()
                .id(1L)
                .title("title 1")
                .author("author 1")
                .publicationYear("2009")
                .ISBN("ISBN 0-061-96436-0")
                .edition(3)
                .bookStatus(BookStatus.AVAILABLE)
                .build();

        BookDTO bookDTO = BookDTO.builder()
                .id(1L)
                .title("title 1")
                .author("author 1")
                .publicationYear("2009")
                .ISBN("ISBN 0-061-96436-0")
                .edition(3)
                .bookStatus(BookStatus.AVAILABLE)
                .build();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.mapBookToDTO(book)).thenReturn(bookDTO);

        BookDTO response = bookService.getBookById(bookId);

        assertEquals(book.getISBN(), response.getISBN());
        assertEquals(book.getEdition(), response.getEdition());

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookMapper, times(1)).mapBookToDTO(book);

    }

    @Test
    public void should_get_books(){
        int page = 0;
        int size = 2;

        Pageable pageable = PageRequest.of(page, size);

        Book book1 = Book.builder()
                .id(1L)
                .title("title 1")
                .author("author 1")
                .publicationYear("2009")
                .ISBN("ISBN 0-061-96436-0")
                .edition(3)
                .bookStatus(BookStatus.AVAILABLE)
                .build();

        Book book2 = Book.builder()
                .id(2L)
                .title("title 2")
                .author("author 2")
                .publicationYear("2010")
                .ISBN("ISBN 0-061-96436-1")
                .edition(1)
                .bookStatus(BookStatus.AVAILABLE)
                .build();

        Book book3 = Book.builder()
                .id(3L)
                .title("title 3")
                .author("author 3")
                .publicationYear("2008")
                .ISBN("ISBN 0-061-96436-9")
                .edition(4)
                .bookStatus(BookStatus.AVAILABLE)
                .build();
        List<Book> books = List.of(book1, book2, book3);
        Page<Book> pageResult = convertListToPage(books, pageable);

        BookDTO bookDTO1 = BookDTO.builder()
                .id(1L)
                .title("title 1")
                .author("author 1")
                .publicationYear("2009")
                .ISBN("ISBN 0-061-96436-0")
                .edition(3)
                .bookStatus(BookStatus.AVAILABLE)
                .build();

        BookDTO bookDTO2 = BookDTO.builder()
                .id(2L)
                .title("title 2")
                .author("author 2")
                .publicationYear("2010")
                .ISBN("ISBN 0-061-96436-1")
                .edition(1)
                .bookStatus(BookStatus.AVAILABLE)
                .build();

        BookDTO bookDTO3 = BookDTO.builder()
                .id(3L)
                .title("title 3")
                .author("author 3")
                .publicationYear("2008")
                .ISBN("ISBN 0-061-96436-9")
                .edition(4)
                .bookStatus(BookStatus.AVAILABLE)
                .build();
        PageableDTO<BookDTO> pageableDTO = new PageableDTO<>(convertListToPage(List.of(bookDTO1, bookDTO2, bookDTO3), pageable));

        when(bookRepository.findAll(pageable)).thenReturn(pageResult);
        when(bookMapper.mapToPageableDTO(pageResult)).thenReturn(pageableDTO);

        PageableDTO<BookDTO> response = bookService.getBooks(page, size);

        assertNotNull(response);
        assertEquals(getPageResultSize(page, size, books.size()), response.getContent().size());
        assertEquals(books.size(), response.getTotalElements());
        assertEquals(page, response.getPageNumber());

        verify(bookRepository, times(1)).findAll(pageable);
        verify(bookMapper, times(1)).mapToPageableDTO(pageResult);

    }

    @Test
    public void should_throw_NoSuchElementException_if_book_not_found_when_updating_book(){
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> bookService.updateBook(anyLong(), BookUpdateInput.class.getDeclaredConstructor().newInstance()));
    }

    @Test
    public void should_update_book(){

        Long bookId = 100L;

        BookUpdateInput updateInput = BookUpdateInput.builder()
                .title("title 12")
                .author("author 12")
                .publicationYear("2003")
                .ISBN("ISBN 0-061-96436-09")
                .edition(3)
                .build();

        BookCreateInput input = BookCreateInput.builder()
                .title("title 12")
                .author("author 12")
                .publicationYear("2003")
                .ISBN("ISBN 0-061-96436-09")
                .edition(3)
                .build();


        Book existingBook = Book.builder()
                .id(1L)
                .title("title 1")
                .author("author 1")
                .publicationYear("2009")
                .ISBN("ISBN 0-061-96436-0")
                .edition(3)
                .bookStatus(BookStatus.AVAILABLE)
                .build();

        BookDTO dto = BookDTO.builder()
                .title("title 12")
                .author("author 12")
                .publicationYear("2003")
                .ISBN("ISBN 0-061-96436-09")
                .edition(3)
                .bookStatus(BookStatus.AVAILABLE)
                .build();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        doNothing().when(bookMapper).bookUpdateInputToBook(updateInput, existingBook);
        when(bookMapper.mapBookToCreateInput(existingBook)).thenReturn(input);
        when(bookRepository.existsByTitleAndAuthorAndPublicationYearAndISBNAndEdition(input.getTitle(), input.getAuthor(), input.getPublicationYear(),input.getISBN(), input.getEdition())).thenReturn(false);
        when(bookRepository.save(existingBook)).thenReturn(existingBook);
        when(bookMapper.mapBookToDTO(existingBook)).thenReturn(dto);

        BookDTO response = bookService.updateBook(bookId, updateInput);

        assertEquals(updateInput.getAuthor(), response.getAuthor());
        assertEquals(updateInput.getISBN(), response.getISBN());
        assertEquals(existingBook.getBookStatus(), response.getBookStatus());

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookMapper, times(1)).bookUpdateInputToBook(updateInput, existingBook);
        verify(bookMapper,times(1)).mapBookToCreateInput(existingBook);
        verify(bookRepository, times(1)).existsByTitleAndAuthorAndPublicationYearAndISBNAndEdition(input.getTitle(), input.getAuthor(), input.getPublicationYear(),input.getISBN(), input.getEdition());
        verify(bookRepository, times(1)).save(existingBook);
        verify(bookMapper, times(1)).mapBookToDTO(existingBook);
    }

    private <T> Page<T> convertListToPage(List<T> list, Pageable pageable) {

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());

        List<T> sublist = list.subList(start, end);

        return new PageImpl<>(sublist, pageable, list.size());
    }

    private int getPageResultSize(int page, int size, int totalElements){
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, totalElements);

        return endIndex - startIndex;
    }
}