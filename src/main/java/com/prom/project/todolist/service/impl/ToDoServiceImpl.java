package com.prom.project.todolist.service.impl;

import com.prom.project.todolist.dto.ToDoDto;
import com.prom.project.todolist.dto.UserDto;
import com.prom.project.todolist.entity.ToDoEntity;
import com.prom.project.todolist.exception.NotFoundException;
import com.prom.project.todolist.mapper.ToDoMapper;
import com.prom.project.todolist.repository.ToDoRepository;
import com.prom.project.todolist.service.ToDoService;
import com.prom.project.todolist.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ToDoServiceImpl implements ToDoService {

    private final ToDoRepository toDoRepository;
    private final ToDoMapper toDoMapper;
    private final UserService userService;

    @Override
    public List<ToDoDto> getToDos() {
        final UserDto user = userService.getCurrentUser();
        var toDoEntities = toDoRepository.findByUsername(user.getUsername());
        log.info("Amount of ToDos : `{}`", toDoEntities.size());
        return toDoMapper.toDtos(toDoEntities);
    }

    @Override
    @Transactional
    public ToDoDto addToDo(final ToDoDto toDoDTO) {
        validateData(toDoDTO);
        final UserDto user = userService.getCurrentUser();
        toDoDTO.setCreatedOn(LocalDate.now());
        toDoDTO.setUsername(user.getUsername());
        log.info("Creating a new ToDo");
        return saveToDB(toDoMapper.toEntity(toDoDTO));
    }

    @Override
    @Transactional
    public ToDoDto updateToDo(final ToDoDto toDoDTO) {
        validateData(toDoDTO);
        final ToDoEntity toDoEntity = toDoRepository.findById(toDoDTO.getId())
                .orElseThrow(() -> new NotFoundException(toDoDTO.getId()));
        if (toDoDTO.isDone()) {
            log.info("ToDo is done, updating completion date");
            toDoDTO.setCompletedOn(LocalDate.now());
        }

        log.info("Updating ToDo with a new data");
        toDoMapper.merge(toDoEntity, toDoDTO);
        return saveToDB(toDoEntity);
    }

    @Override
    @Transactional
    public void deleteToDo(final Integer id) {
        log.info("Deleting ToDo...");
        if (!toDoRepository.existsById(id)) {
            throw new NotFoundException(id);
        }
        log.debug("ToDo exists with id `{}`", id);
        toDoRepository.deleteById(id);
        log.info("ToDo removed successfully");
    }

    private void validateData(final ToDoDto toDoDTO) {
        log.info("Validating ToDoDTO...");
        if (Objects.isNull(toDoDTO)) {
            log.error("ToDoDTO is null");
            throw new NullPointerException("There is no data to add");
        }
        if (!StringUtils.hasText(toDoDTO.getDescription())) {
            log.error("Description is empty");
            throw new NullPointerException("There is no description provided");
        }
        log.info("ToDoDTO is valid");
    }

    private ToDoDto saveToDB(final ToDoEntity toDoEntity) {
        return toDoMapper.toDto(toDoRepository.save(toDoEntity));
    }
}
