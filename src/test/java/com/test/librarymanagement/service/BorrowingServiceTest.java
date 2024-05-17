package com.test.librarymanagement.service;

import com.test.librarymanagement.domain.data.Book;
import com.test.librarymanagement.domain.data.BorrowingRecord;
import com.test.librarymanagement.domain.data.Patron;
import com.test.librarymanagement.domain.dto.BorrowingRecordDTO;
import com.test.librarymanagement.domain.enums.BookStatus;
import com.test.librarymanagement.domain.enums.Status;
import com.test.librarymanagement.exception.LibraryManagementException;
import com.test.librarymanagement.mapper.BorrowingRecordMapper;
import com.test.librarymanagement.repository.BookRepository;
import com.test.librarymanagement.repository.BorrowingRecordRepository;
import com.test.librarymanagement.repository.PatronRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BorrowingServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PatronRepository patronRepository;

    @Mock
    private BorrowingRecordRepository borrowingRecordRepository;

    @Mock
    private BorrowingRecordMapper borrowingRecordMapper;

    @InjectMocks
    private BorrowingService borrowingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void should_borrow_book(){
        Long bookId = 1L;
        Long patronId = 2L;

        Book book = Book.builder()
                .id(bookId)
                .title("title 1")
                .author("author 1")
                .publicationYear("2009")
                .ISBN("ISBN 0-061-96436-0")
                .edition(3)
                .bookStatus(BookStatus.AVAILABLE)
                .build();

        Patron patron = Patron.builder()
                .id(patronId)
                .firstName("first")
                .lastName("Last")
                .Address("Lagos 1")
                .phonenumber("08089765376")
                .build();

        BorrowingRecord record = BorrowingRecord.builder()
                .book(book.getId())
                .patron(patron.getId())
                .status(Status.BORROWED)
                .build();

        BorrowingRecordDTO dto = BorrowingRecordDTO.builder()
                .book(book.getId())
                .patron(patron.getId())
                .status(Status.BORROWED)
                .build();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(patronRepository.findById(patronId)).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.save(record)).thenReturn(record);
        when(borrowingRecordMapper.mapToDTO(record)).thenReturn(dto);

        BorrowingRecordDTO response = borrowingService.borrowBook(bookId, patronId);

        assertEquals(bookId, response.getBook());
        assertEquals(patronId, response.getPatron());

        verify(bookRepository, times(1)).findById(bookId);
        verify(patronRepository, times(1)).findById(patronId);
        verify(borrowingRecordRepository, times(1)).save(record);
        verify(borrowingRecordMapper, times(1)).mapToDTO(record);
    }

    @Test
    public void should_throw_LibraryManagementException_when_borrowing_book_if_book_is_unavailable(){
        Long bookId = 1L;
        Long patronId = 2L;

        Book book = Book.builder()
                .id(bookId)
                .title("title 1")
                .author("author 1")
                .publicationYear("2009")
                .ISBN("ISBN 0-061-96436-0")
                .edition(3)
                .bookStatus(BookStatus.UNAVAILABLE)
                .build();

        Patron patron = Patron.builder()
                .id(patronId)
                .firstName("first")
                .lastName("Last")
                .Address("Lagos 1")
                .phonenumber("08089765376")
                .build();

        BorrowingRecord record = BorrowingRecord.builder()
                .book(book.getId())
                .patron(patron.getId())
                .status(Status.BORROWED)
                .returnedAt(null)
                .build();

        BorrowingRecordDTO dto = BorrowingRecordDTO.builder()
                .book(book.getId())
                .patron(patron.getId())
                .status(Status.BORROWED)
                .build();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(patronRepository.findById(patronId)).thenReturn(Optional.of(patron));

        assertThrows(LibraryManagementException.class, () -> borrowingService.borrowBook(bookId, patronId));
    }

    @Test
    public void should_return_book(){

        Long bookId = 1L;
        Long patronId = 2L;

        Book book = Book.builder()
                .id(bookId)
                .title("title 1")
                .author("author 1")
                .publicationYear("2009")
                .ISBN("ISBN 0-061-96436-0")
                .edition(3)
                .bookStatus(BookStatus.UNAVAILABLE)
                .build();

        Patron patron = Patron.builder()
                .id(patronId)
                .firstName("first")
                .lastName("Last")
                .Address("Lagos 1")
                .phonenumber("08089765376")
                .build();

        BorrowingRecord existingRecord = BorrowingRecord.builder()
                .book(book.getId())
                .patron(patron.getId())
                .status(Status.BORROWED)
                .build();

        BorrowingRecordDTO dto = BorrowingRecordDTO.builder()
                .book(book.getId())
                .patron(patron.getId())
                .status(existingRecord.getStatus())
                .build();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(patronRepository.findById(patronId)).thenReturn(Optional.of(patron));
        when(borrowingRecordRepository.findByBookAndPatronAndStatus(book.getId(), patron.getId(), Status.BORROWED)).thenReturn(Optional.of(existingRecord));
        when(borrowingRecordRepository.save(existingRecord)).thenReturn(existingRecord);
        when(borrowingRecordMapper.mapToDTO(existingRecord)).thenReturn(dto);

        borrowingService.returnBook(bookId, patronId);


        verify(bookRepository, times(1)).findById(bookId);
        verify(patronRepository, times(1)).findById(patronId);
        verify(borrowingRecordRepository, times(1)).findByBookAndPatronAndStatus(book.getId(), patron.getId(), Status.BORROWED);
        verify(borrowingRecordRepository, times(1)).save(existingRecord);
        verify(borrowingRecordMapper, times(1)).mapToDTO(existingRecord);
    }
}