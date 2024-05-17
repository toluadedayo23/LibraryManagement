package com.test.librarymanagement.config.security;


import com.test.librarymanagement.domain.data.User;
import com.test.librarymanagement.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthJwtService jwtService;
    private final UserService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            if(request.getServletPath().startsWith("/api/books") || request.getServletPath().startsWith("/api/patrons") || request.getServletPath().startsWith("/api/borrow") || request.getServletPath().startsWith("/api/return")) {
                this.returnErrorMessageWhenJWTEmpty(response);
                return;
            }

            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(7);
        try {
            username = jwtService.extractUsername(jwt);
        } catch (Exception e) {
            handleJwtException(e, response);
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User userDetails = this.userDetailsService.loadByUsername(username);
            try{

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }catch(Exception e){
                handleJwtException(e, response);
            }
        }
        filterChain.doFilter(request, response);
    }

    private void returnErrorMessageWhenJWTEmpty(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\":\"Authorization token is missing. Please include a valid authorization token in the request header\" } ");
    }

    private void handleJwtException(Exception exception, HttpServletResponse response) throws IOException {

        log.error("JWT exception occurred");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        String errorMessage = switch (exception.getClass().getSimpleName()) {
            case "UnsupportedJwtException" -> "Jwt format does not match the expected format expected by the application";
            case "ExpiredJwtException" -> "JWT token has expired";
            case "MalformedJwtException" -> "JWT token is malformed";
            case "SignatureException" -> "JWT token signature is invalid";
            default -> "Invalid JWT token";
        };
        response.getWriter().write(String.format("{ \"error\":  \"%s\" }", errorMessage));
    }


}
