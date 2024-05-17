package com.test.librarymanagement.repository;

import com.test.librarymanagement.domain.data.BorrowingRecord;
import com.test.librarymanagement.domain.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {

    Optional<BorrowingRecord> findByBookAndPatronAndStatus(Long book, Long patron, Status status);
}
