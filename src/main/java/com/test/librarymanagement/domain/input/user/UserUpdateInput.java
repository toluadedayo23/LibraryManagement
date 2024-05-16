package com.test.librarymanagement.domain.input.user;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class UserUpdateInput {

    private String email;
}
