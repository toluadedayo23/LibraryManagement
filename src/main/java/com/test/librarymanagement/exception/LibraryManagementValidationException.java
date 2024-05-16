package com.test.librarymanagement.exception;

import lombok.Getter;

import java.util.Set;

@Getter
public class LibraryManagementValidationException extends LibraryManagementException {


    private final Set<String> errors;

    public LibraryManagementValidationException(String message, Set<String> errors) {
        super(message.concat(" ").concat(errors.toString()));
        this.errors = errors;
    }

    public LibraryManagementValidationException(String message) {
        super(message);
        this.errors = Set.of(message);
    }

}
