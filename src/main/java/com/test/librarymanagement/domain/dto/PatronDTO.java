package com.test.librarymanagement.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class PatronDTO {

    private Long id;

    private String firstName;

    private String lastName;

    private String Address;

    private String phonenumber;
}
