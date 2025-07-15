package com.prom.project.todolist.service.impl;

import com.prom.project.todolist.dto.ToDoDto;
import com.prom.project.todolist.entity.ToDoEntity;
import com.prom.project.todolist.exception.NotFoundException;
import com.prom.project.todolist.mapper.ToDoMapper;
import com.prom.project.todolist.repository.ToDoRepository;
import com.prom.project.todolist.service.ToDoService;
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

    @Override
    public List<ToDoDto> getToDos() {
        var toDoEntities = toDoRepository.findAll();
        log.info("Amount of ToDos : `{}`", toDoEntities.size());
        return toDoMapper.toDtos(toDoEntities);
    }

    @Override
    public ToDoDto addToDo(final ToDoDto toDoDTO) {
        validateData(toDoDTO);
        toDoDTO.setCreatedOn(LocalDate.now());
        log.info("Creating a new ToDo");
        var toDoEntity = toDoMapper.toEntity(toDoDTO);
        return saveToDB(toDoEntity);
    }

    @Override
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
