package com.test.librarymanagement.service;

import com.test.librarymanagement.config.security.AuthJwtService;
import com.test.librarymanagement.domain.data.Token;
import com.test.librarymanagement.domain.data.User;
import com.test.librarymanagement.domain.input.LoginInput;
import com.test.librarymanagement.repository.TokenRepository;
import com.test.librarymanagement.repository.UserRepository;
import com.test.librarymanagement.response.JwtResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final AuthJwtService jwtService;
    private final TokenRepository tokenRepository;

    public JwtResponse authenticateUser(LoginInput loginRequest) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername().trim(),
                            loginRequest.getPassword().trim()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = userService.loadByUsername(authentication.getName());
            Token jwt = jwtService.generateToken(user);

            return new JwtResponse(jwt, user);
    }

    public JwtResponse refreshToken(String token) {
        String username = jwtService.validateRefreshToken(token);
        User user = userService.loadByUsername(username);
        Token jwt = jwtService.generateToken(user);
        tokenRepository.deleteByRefreshToken(token);
        return new JwtResponse(jwt, user);
    }
}
