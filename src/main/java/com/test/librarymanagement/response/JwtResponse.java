package com.test.librarymanagement.response;


import com.test.librarymanagement.domain.data.Token;
import com.test.librarymanagement.domain.data.User;
import com.test.librarymanagement.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String refreshToken;
    private final String type = "Bearer";
    private List<Map<String, Role>> roles;

    public JwtResponse(Token tokenStore, User user) {
        this.token = tokenStore.getAccessToken();
        this.refreshToken = tokenStore.getRefreshToken();
        this.roles = user.getAuthorities().stream()
                .map(grantedAuthority -> Map.of("authority", Role.valueOf(grantedAuthority.getAuthority())))
                .collect(Collectors.toList());
    }
}
