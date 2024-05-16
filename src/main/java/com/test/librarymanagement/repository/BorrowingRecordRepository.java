package com.test.librarymanagement.repository;

import com.test.librarymanagement.domain.data.BorrowingRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowingRecordRepository extends JpaRepository<BorrowingRecord, Long> {
}
