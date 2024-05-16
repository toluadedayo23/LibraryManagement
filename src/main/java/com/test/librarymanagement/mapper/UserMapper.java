package com.test.librarymanagement.mapper;

import com.test.librarymanagement.domain.data.User;
import com.test.librarymanagement.domain.dto.PageableDTO;
import com.test.librarymanagement.domain.dto.UserDTO;
import com.test.librarymanagement.domain.input.user.UserCreateInput;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "username", source = "input.email")
    User mapToUser(UserCreateInput input);

    UserDTO mapToDTO(User user);


    @Mapping(target = "pageNumber", source = "number")
    PageableDTO<UserDTO> mapToPageableDTO(Page<User> result);
}
