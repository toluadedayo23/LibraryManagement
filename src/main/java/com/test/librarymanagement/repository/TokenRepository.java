package com.test.librarymanagement.repository;

import com.test.librarymanagement.domain.data.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByAccessToken(String token);

    Optional<Token> findByRefreshToken(String token);

    @Modifying
    @Transactional
    void deleteByAccessToken(String token);
}
