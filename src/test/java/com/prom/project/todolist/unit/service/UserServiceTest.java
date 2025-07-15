package com.prom.project.todolist.unit.service;

import com.prom.project.todolist.dto.TokenResponse;
import com.prom.project.todolist.dto.UserDto;
import com.prom.project.todolist.entity.UserEntity;
import com.prom.project.todolist.mapper.UserMapper;
import com.prom.project.todolist.repository.UserRepository;
import com.prom.project.todolist.service.impl.JwtGeneratorImpl;
import com.prom.project.todolist.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository repository;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private AuthenticationManager authManager;
    @Mock
    private JwtGeneratorImpl jwtService;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void registerPersistsEncodedPassword() {
        final UserDto dto = new UserDto()
                .setUsername("test")
                .setPassword("test");

        final UserEntity user = new UserEntity();
        user.setUsername("test");
        user.setPassword("test");

        final UserDetails userDetails = new UserDto();
        final Authentication authResponse = new UsernamePasswordAuthenticationToken(userDetails, null);

        when(repository.existsByUsername(dto.getUsername())).thenReturn(false);
        when(authManager.authenticate(any())).thenReturn(authResponse);
        when(userMapper.toUserEntity(dto, encoder)).thenReturn(user);
        when(jwtService.generateJwt(userDetails)).thenReturn(new TokenResponse("jwt", new Date(), new Date()));

        TokenResponse token = userService.register(dto);

        assertThat(token.getToken()).isEqualTo("jwt");
    }
}
