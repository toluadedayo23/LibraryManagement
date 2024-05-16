package com.test.librarymanagement.exception;

import lombok.Data;

@Data
public class LibraryManagementException extends RuntimeException {
    private static final long serialVersionUID = -2132835818008187546L;

    private final String message;

    public LibraryManagementException(String message) {
        super(message);
        this.message = message;
    }

    public String getErrorMessage() {
        return this.message;
    }
}
