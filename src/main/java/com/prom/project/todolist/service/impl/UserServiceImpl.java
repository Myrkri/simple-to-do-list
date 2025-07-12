package com.prom.project.todolist.service.impl;

import com.prom.project.todolist.dto.UserDto;
import com.prom.project.todolist.entity.UserEntity;
import com.prom.project.todolist.mapper.UserMapper;
import com.prom.project.todolist.repository.UserRepository;
import com.prom.project.todolist.service.JwtGenerator;
import com.prom.project.todolist.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtGenerator jwtGenerator;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(final UserDto user) {
        if (!userRepository.existsByUsername(user.getUsername())) {
            log.info("Username `{}` doesn't exist", user.getUsername());
            throw new IllegalStateException("User isn't registered");
        }
        final Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        return jwtGenerator.generateJwt((UserDetails) auth.getPrincipal());
    }

    @Override
    public String register(final UserDto user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            log.info("Username `{}` is already in use", user.getUsername());
            throw new IllegalStateException("Username already exists");
        }
        final UserEntity userEntity = userMapper.toUserEntity(user, passwordEncoder);
        userRepository.save(userEntity);
        log.info("User `{}` registered", user.getUsername());
        return login(user);
    }

    @Override
    public UserDto getCurrentUser() {
        return null; //TODO return current user from token
    }
}
