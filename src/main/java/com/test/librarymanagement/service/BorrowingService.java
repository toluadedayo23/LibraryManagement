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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class BorrowingService {

    private final BookRepository bookRepository;
    private final PatronRepository patronRepository;
    private final BorrowingRecordRepository borrowingRecordRepository;
    private final BorrowingRecordMapper borrowingRecordMapper;

    public BorrowingRecordDTO borrowBook(Long bookId, Long patronId){

        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new NoSuchElementException("Book not found")
        );

        Patron patron = patronRepository.findById(patronId).orElseThrow(
                () -> new NoSuchElementException("Patron not found")
        );

        if(book.getBookStatus().equals(BookStatus.UNAVAILABLE)){
            throw new LibraryManagementException("Book not available");
        }

        BorrowingRecord record = BorrowingRecord.builder()
                .book(book.getId())
                .patron(patron.getId())
                .status(Status.BORROWED)
                .build();

        return borrowingRecordMapper.mapToDTO(borrowingRecordRepository.save(record));
    }

    public BorrowingRecordDTO returnBook(Long bookId, Long patronId){

        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new NoSuchElementException("Book not found")
        );

        Patron patron = patronRepository.findById(patronId).orElseThrow(
                () -> new NoSuchElementException("Patron not found")
        );

        BorrowingRecord existingBorrowedRecord = borrowingRecordRepository.findByBookAndPatronAndStatus(book.getId(), patron.getId(), Status.BORROWED).orElseThrow(
                () -> new NoSuchElementException("No Borrowed record found")
        );

        existingBorrowedRecord.setStatus(Status.RETURNED);
        existingBorrowedRecord.setReturnedAt(LocalDateTime.now());

        return borrowingRecordMapper.mapToDTO(borrowingRecordRepository.save(existingBorrowedRecord));
    }
}
