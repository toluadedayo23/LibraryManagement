package com.test.librarymanagement.repository;

import com.test.librarymanagement.domain.data.Patron;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatronRepository extends JpaRepository<Patron, Long> {
}
