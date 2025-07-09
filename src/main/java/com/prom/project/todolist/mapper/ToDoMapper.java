package com.prom.project.todolist.mapper;

import com.prom.project.todolist.dto.ToDoDto;
import com.prom.project.todolist.entity.ToDoEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ToDoMapper {

    ToDoDto toDto(ToDoEntity toDoEntity);
    ToDoEntity toEntity(ToDoDto toDoDto);
    List<ToDoDto> toDtos(List<ToDoEntity> toDoEntities);
    void merge(@MappingTarget ToDoEntity toDoEntity, ToDoDto toDoDto);

}
