package com.test.librarymanagement.service;

import com.test.librarymanagement.mapper.PatronMapper;
import com.test.librarymanagement.repository.PatronRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PatronService {

    private final PatronRepository patronRepository;
    private final PatronMapper patronMapper;

}
