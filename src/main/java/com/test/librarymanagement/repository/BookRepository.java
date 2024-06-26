package com.test.librarymanagement.repository;

import com.test.librarymanagement.domain.data.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByTitleAndAuthorAndPublicationYearAndISBNAndEdition(String title, String author, String publicationYear, String ISBN, Integer edition);
}
