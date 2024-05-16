package com.test.librarymanagement.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.test.librarymanagement.domain.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private Long id;

    private String username;

    private String email;

    private Boolean enabled;

    private Role role;

}
