package com.test.librarymanagement.service;

import com.test.librarymanagement.config.security.AuthJwtService;
import com.test.librarymanagement.domain.data.Token;
import com.test.librarymanagement.domain.data.User;
import com.test.librarymanagement.domain.enums.Role;
import com.test.librarymanagement.domain.input.LoginInput;
import com.test.librarymanagement.repository.TokenRepository;
import com.test.librarymanagement.response.JwtResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserService userService;
    @Mock
    private AuthJwtService jwtService;
    @Mock
    private TokenRepository tokenRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void should_successfully_authenticate_User() {

        Authentication authentication = new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority(Role.LIBRARIAN.name()));
            }

            @Override
            public Object getCredentials() {
                return "password";
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return "librarian1@gmail.com";
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return "librarian1@gmail.com";
            }
        };

        User user = User.builder()
                .id(1L)
                .enabled(true)
                .role(Role.LIBRARIAN)
                .username("librarian1@gmail.com")
                .email("librarian1@gmail.com")
                .password("password").build();

        LoginInput loginInput = LoginInput.builder().username("librarian1@gmail.com").password("password").build();

        Token token = new Token(1L, "gdgaggaha", "hqdshhdhd", "librarian1@gmail.com");


        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);


        when(userService.loadByUsername(authentication.getName())).thenReturn(user);

        when(jwtService.generateToken(any(User.class))).thenReturn(token);


        JwtResponse response = authenticationService.authenticateUser(loginInput);

        Assertions.assertNotNull(response);
        assertEquals(token.getAccessToken(), response.getToken());
        assertEquals(token.getRefreshToken(), response.getRefreshToken());
        assertEquals(user.getRole(), response.getRoles().stream().findFirst().get().get("authority"));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userService, times(1)).loadByUsername(authentication.getName());
        verify(jwtService, times(1)).generateToken(user);

    }

    @Test
    public void should_successfully_refreshToken() {
        String refreshToken = "hdscjjdjhdcjjdjh";

        String username = "librarian1@gmail.com";


        User user = User.builder()
                .id(1L)
                .enabled(true)
                .role(Role.LIBRARIAN)
                .username("librarian1@gmail.com")
                .email("librarian1@gmail.com")
                .password("password").build();

        Token token = new Token(1L, "gdgaggaha", "hdscjjdjhdcjjdjh", "librarian1@gmail.com");

        when(jwtService.validateRefreshToken(refreshToken)).thenReturn(username);
        when(userService.loadByUsername(username)).thenReturn(user);
        when(jwtService.generateToken(user)).thenReturn(token);

        doNothing().when(tokenRepository).deleteByRefreshToken(refreshToken);

        JwtResponse response = authenticationService.refreshToken(refreshToken);

        Assertions.assertNotNull(response);
        assertEquals(token.getAccessToken(), response.getToken());
        assertEquals(token.getRefreshToken(), response.getRefreshToken());
        assertEquals(user.getRole(), response.getRoles().stream().findFirst().get().get("authority"));

        verify(jwtService, times(1)).validateRefreshToken(refreshToken);
        verify(userService, times(1)).loadByUsername(username);
        verify(jwtService, times(1)).generateToken(user);
        verify(tokenRepository, times(1)).deleteByRefreshToken(refreshToken);

    }

}