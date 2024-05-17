package com.test.librarymanagement.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@NoArgsConstructor
public class PatronDTO implements Serializable {

    private Long id;

    private String firstName;

    private String lastName;

    private String address;

    private String phonenumber;
}
