package com.test.librarymanagement.domain.input.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class UserCreateInput {

    @NotNull
    @Size(min = 8, max = 30, message = "Email cannot be less than 8 or more than 30 characters")
    private String email;

}
