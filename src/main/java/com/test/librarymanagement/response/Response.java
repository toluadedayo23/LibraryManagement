package com.test.librarymanagement.response;

public interface Response {
    String SUCCESS_MESSAGE = "Success";

    default String getMessage() {
        return SUCCESS_MESSAGE;
    }
}
