package com.test.librarymanagement.controller;

import com.test.librarymanagement.domain.dto.BorrowingRecordDTO;
import com.test.librarymanagement.service.BorrowingService;
import com.test.librarymanagement.util.WebSecurityUtil;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("api/return/")
@PreAuthorize("hasAuthority('LIBRARIAN')")
public class ReturnBookController {

    private final BorrowingService borrowingService;
    private final WebSecurityUtil webSecurityUtil;

    @PostMapping("{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowingRecordDTO> returnBook(
            @PathVariable(value = "bookId") @Min(value = 1) Long bookId,
            @PathVariable(value = "patronId") @Min(value = 1) Long patronId
    ){
        log.info("Librarian: {} returning Book: {} for Patron: {}", webSecurityUtil.getUserForLog(), bookId, patronId);

        BorrowingRecordDTO dto = borrowingService.returnBook(bookId, patronId);

        log.info("Librarian: {} returned Book: {} for Patron: {}", webSecurityUtil.getUserForLog(), bookId, patronId);

        return ResponseEntity.ok(dto);

    }
}
