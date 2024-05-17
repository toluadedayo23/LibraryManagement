package com.test.librarymanagement.controller;

import com.test.librarymanagement.util.WebSecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("api/user/")
public class UserController {
    private final WebSecurityUtil webSecurityUtil;
    private final LogoutHandler logoutHandler;
    @PostMapping("logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response){
        log.info("User: {} logging out", webSecurityUtil.getUserForLog());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        logoutHandler.logout(request, response, authentication);

        log.info("User: {} logged out", webSecurityUtil.getUserForLog());

        return ResponseEntity.noContent().build();
    }
}
