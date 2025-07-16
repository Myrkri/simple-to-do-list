package com.prom.project.todolist.intg.service;

import com.prom.project.todolist.dto.TokenResponse;
import com.prom.project.todolist.dto.UserDto;
import com.prom.project.todolist.repository.UserRepository;
import com.prom.project.todolist.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
public class UserServiceIntgTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    void registerNewUser() {
        final UserDto user = new UserDto()
                .setUsername("test")
                .setPassword("test");

        final TokenResponse result = userService.register(user);

        var entity = userRepository.findByUsername("test")
                .orElse(null);

        assertTrue(StringUtils.hasText(result.getToken()), "Token not found");
        assertNotNull(entity, "Response from DB must not be null");
        assertEquals(user.getUsername(), entity.getUsername(), "Username mismatch");
    }

    @Test
    void getCurrentUser() {
        final Jwt jwt = Jwt.withTokenValue("test-token")
                .subject("test-user")
                .header("test-header", "nothing")
                .claim("roles", List.of("ROLE_USER"))
                .build();

        final AbstractAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(jwt, null,
                        AuthorityUtils.createAuthorityList("ROLE_USER"));

        SecurityContextHolder.getContext().setAuthentication(auth);

        final UserDto dto = userService.getCurrentUser();

        assertEquals("test-user", dto.getUsername());
        assertEquals("[ROLE_USER]", dto.getRole());
    }
}
