package com.test.librarymanagement.controller;

import com.test.librarymanagement.domain.input.LoginInput;
import com.test.librarymanagement.response.JwtResponse;
import com.test.librarymanagement.service.AuthenticationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("api/auth/")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("signin")
    public ResponseEntity<JwtResponse> authenticateUsers(@Valid @RequestBody LoginInput loginInput){

        return ResponseEntity.ok(authenticationService.authenticateUser(loginInput));
    }

    @GetMapping("refresh")
    public ResponseEntity<JwtResponse> refreshToken(@RequestParam(name = "token")
                                                    @NotBlank(message = "refresh token cannot be blank or null")
                                                    String token){
        return ResponseEntity.ok(authenticationService.refreshToken(token));
    }
}
