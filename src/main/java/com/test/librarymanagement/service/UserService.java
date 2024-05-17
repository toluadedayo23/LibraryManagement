package com.test.librarymanagement.service;

import com.test.librarymanagement.domain.data.User;
import com.test.librarymanagement.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public User loadByUsername(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(NoSuchElementException::new);
    }


}
