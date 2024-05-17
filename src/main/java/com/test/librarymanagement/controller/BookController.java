package com.test.librarymanagement.controller;

import com.test.librarymanagement.domain.dto.BookDTO;
import com.test.librarymanagement.domain.dto.PageableDTO;
import com.test.librarymanagement.domain.input.book.BookCreateInput;
import com.test.librarymanagement.domain.input.book.BookUpdateInput;
import com.test.librarymanagement.response.PageableDataResponse;
import com.test.librarymanagement.service.BookService;
import com.test.librarymanagement.util.WebSecurityUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("api/books/")
@PreAuthorize("hasAuthority('LIBRARIAN')")
public class BookController {

    private final BookService bookService;
    private final WebSecurityUtil webSecurityUtil;

    @PostMapping
    public ResponseEntity<BookDTO> addBook(@Valid @RequestBody BookCreateInput input){
        log.info("Librarian: {} adding book", webSecurityUtil.getUserForLog());

        BookDTO bookDTO = bookService.addBook(input);

        log.info("Librarian: {} added book: {}", webSecurityUtil.getUserForLog(), bookDTO.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(bookDTO);
    }

    @GetMapping("{id}")
    public ResponseEntity<BookDTO> getBookById(
            @PathVariable(value = "id") @Min(value = 1) Long id
    ){
        log.info("Librarian: {} fetching book: {}", webSecurityUtil.getUserForLog(), id);

        BookDTO dto = bookService.getBookById(id);

        log.info("Librarian: {} fetched book: {}", webSecurityUtil.getUserForLog(), id);

        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<PageableDataResponse<BookDTO>> getBooks(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "0") int size
    ){
        log.info("Librarian: {} fetching books", webSecurityUtil.getUserForLog());

        PageableDTO<BookDTO> books = bookService.getBooks(page, size);

        log.info("Librarian: {} fetched books", webSecurityUtil.getUserForLog());

        return ResponseEntity.ok(new PageableDataResponse<>(books));
    }

    @PutMapping("{id}")
    public ResponseEntity<BookDTO> updateBook(
            @PathVariable(value = "id") @Min(value = 1) Long id,
            @Valid @RequestBody BookUpdateInput updateInput
            ){

        log.info("Librarian: {} updating book: {}", webSecurityUtil.getUserForLog(), id);

        BookDTO dto = bookService.updateBook(id, updateInput);

        log.info("Librarian: {} updated book: {}", webSecurityUtil.getUserForLog(), id);

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> removeBook(@PathVariable(value = "id") @Min(value = 1) Long id){
        log.info("Librarian: {} deleting book: {}", webSecurityUtil.getUserForLog(), id);

        bookService.removeBook(id);

        log.info("Librarian: {} deleted book: {}", webSecurityUtil.getUserForLog(), id);

        return ResponseEntity.noContent().build();
    }

}
