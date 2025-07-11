package com.prom.project.todolist.mapper;

import com.prom.project.todolist.dto.UserDto;
import com.prom.project.todolist.entity.UserEntity;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(UserEntity user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password",
            expression = "java(passwordEncoder.encode(userDto.getPassword()))")
    UserEntity toUserEntity(UserDto userDto, @Context PasswordEncoder passwordEncoder);

}