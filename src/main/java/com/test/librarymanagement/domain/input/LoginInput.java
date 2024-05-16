package com.test.librarymanagement.domain.input;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class LoginInput {

    @NotNull
    @Size(min = 8, max = 30, message = "Username cannot be less than 8 or more than 30 characters")
    private String username;

    @NotNull
    @Size(min = 8, max = 20)
    private String password;
}
