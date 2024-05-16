package com.test.librarymanagement.service;

import com.test.librarymanagement.mapper.BookMapper;
import com.test.librarymanagement.repository.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
}
