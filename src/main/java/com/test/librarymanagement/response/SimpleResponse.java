package com.test.librarymanagement.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class SimpleResponse implements Response {
    private String message;
}
