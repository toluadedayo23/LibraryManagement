package com.test.librarymanagement.service;

import com.test.librarymanagement.domain.data.User;
import com.test.librarymanagement.domain.dto.UserDTO;
import com.test.librarymanagement.domain.enums.Role;
import com.test.librarymanagement.domain.input.user.UserCreateInput;
import com.test.librarymanagement.mapper.UserMapper;
import com.test.librarymanagement.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    public User loadByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(NoSuchElementException::new);
    }

    public UserDTO createLibrarian(UserCreateInput userCreateInput){
        User user = userMapper.mapToUser(userCreateInput);
        String password = RandomStringUtils.randomAlphanumeric(10);
        user.setPassword(passwordEncoder.encode(password));
        user.setEnabled(true);
        user.setRole(Role.LIBRARIAN);

        // if email service is enabled, generate random password
        // for each librarian and force them to change it to personal
        // password on first time login.
        // or else cannot use the application

        return userMapper.mapToDTO(userRepository.save(user));
    }


}
