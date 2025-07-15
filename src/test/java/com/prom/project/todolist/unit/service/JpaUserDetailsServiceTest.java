package com.prom.project.todolist.unit.service;

import com.prom.project.todolist.dto.UserDto;
import com.prom.project.todolist.entity.UserEntity;
import com.prom.project.todolist.mapper.UserMapper;
import com.prom.project.todolist.repository.UserRepository;
import com.prom.project.todolist.service.impl.JpaUserDetailsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JpaUserDetailsServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private JpaUserDetailsService service;

    @Test
    void loadUserByUsername_returnsMappedUserDetails() {
        final String username = "test";
        final UserDto expected = new UserDto()
                .setUsername(username)
                .setPassword("password");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new UserEntity()));
        when(userMapper.toUserDto(any())).thenReturn(expected);

        final UserDetails result = service.loadUserByUsername(username);

        assertEquals(expected, result);
    }
}
