package com.test.librarymanagement.domain.input.patron;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class PatronUpdateInput {

    private String firstName;

    private String lastName;

    private String Address;

    private String phonenumber;
}
