package com.prom.project.todolist.mapper;

import com.prom.project.todolist.dto.UserDto;
import com.prom.project.todolist.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(UserEntity user);
    @Mapping(target = "id", ignore = true)
    UserEntity toUserEntity(UserDto userDto);

}