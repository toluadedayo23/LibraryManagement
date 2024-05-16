package com.test.librarymanagement.domain.input.patron;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class PatronCreateInput {

    @NotNull
    @Size(min = 4, max = 20, message = "First name cannot be less than 4 or more than 20 characters")
    private String firstName;

    @NotNull
    @Size(min = 4, max = 20, message = "Last name cannot be less than 4 or more than 20 characters")
    private String lastName;

    @NotNull
    @Size(min = 10, max = 70, message = "Address cannot be less than 10 or more than 70 characters")
    private String Address;

    @NotNull
    @Size(min = 11, max = 13, message = "Phone number cannot be less than 11 characters and more than 13 characters")
    private String phonenumber;
}
