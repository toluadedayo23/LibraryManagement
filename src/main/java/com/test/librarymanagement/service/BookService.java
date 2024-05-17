package com.test.librarymanagement.service;

import com.test.librarymanagement.domain.data.Book;
import com.test.librarymanagement.domain.dto.BookDTO;
import com.test.librarymanagement.domain.dto.PageableDTO;
import com.test.librarymanagement.domain.enums.BookStatus;
import com.test.librarymanagement.domain.input.book.BookCreateInput;
import com.test.librarymanagement.domain.input.book.BookUpdateInput;
import com.test.librarymanagement.mapper.BookMapper;
import com.test.librarymanagement.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import static com.test.librarymanagement.config.cache.CacheConfig.BOOK_PAGE_RESULT_CACHE;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    public BookDTO addBook(BookCreateInput input){

        if(checkIfBookExists(input)){
            throw  new IllegalArgumentException("Book already exists");
        }

        Book book = bookMapper.mapInputToBook(input);
        book.setBookStatus(BookStatus.AVAILABLE);

        return bookMapper.mapBookToDTO(bookRepository.save(book));
    }

    public BookDTO getBookById(Long bookId){
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new NoSuchElementException("Book not found")
        );

        return bookMapper.mapBookToDTO(book);
    }

    @Cacheable(value = BOOK_PAGE_RESULT_CACHE, key = "'books'")
    public PageableDTO<BookDTO> getBooks(int page, int size){
        if (size <= 0) {
            size = 100;
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Book> result = bookRepository.findAll(pageable);

        return bookMapper.mapToPageableDTO(result);
    }

    public BookDTO updateBook(Long bookId, BookUpdateInput updateInput){

        Book existingBook = bookRepository.findById(bookId).orElseThrow(
                () -> new NoSuchElementException("Book not found")
        );

        bookMapper.bookUpdateInputToBook(updateInput, existingBook);

        if(checkIfBookExists(bookMapper.mapBookToCreateInput(existingBook))){
            throw  new IllegalArgumentException("Cannot complete update, book with update input already exist");
        }

        return bookMapper.mapBookToDTO(bookRepository.save(existingBook));

    }

    public void removeBook(Long id){
        bookRepository.deleteById(id);
    }

    private Boolean checkIfBookExists(BookCreateInput input){
        return bookRepository.existsByTitleAndAuthorAndPublicationYearAndISBNAndEdition(input.getTitle(), input.getAuthor(), input.getPublicationYear(), input.getISBN(), input.getEdition());
    }
}
