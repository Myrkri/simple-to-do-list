package com.prom.project.todolist.service.impl;

import com.prom.project.todolist.dto.ToDoDto;
import com.prom.project.todolist.entity.ToDoEntity;
import com.prom.project.todolist.mapper.ToDoMapper;
import com.prom.project.todolist.repository.ToDoRepository;
import com.prom.project.todolist.service.ToDoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    //TODO move duplicated code from create and update methods to separate methods
    @Override
    public ToDoDto addToDo(final ToDoDto toDoDTO) {
        validateData(toDoDTO);
        toDoDTO.setCreatedOn(LocalDate.now());
        log.info("Creating a new ToDo");
        var toDoEntity = toDoMapper.toEntity(toDoDTO);
        return toDoMapper.toDto(toDoRepository.save(toDoEntity));
    }

    //TODO get record by ID from DB and MERGE data
    @Override
    public ToDoDto updateToDo(final ToDoDto toDoDTO) {
        validateData(toDoDTO);
        final Optional<ToDoEntity> optEntity = toDoRepository.findById(toDoDTO.getId());
        if (optEntity.isEmpty()) {
            //TODO think about existence check
            throw new RuntimeException();
        }
        log.info("Updating ToDo with a new data");
        final ToDoEntity toDoEntity = optEntity.get();
        toDoMapper.merge(toDoEntity, toDoDTO);

        if (toDoDTO.isDone()) {
            log.info("ToDo is done, updating completion date");
            toDoDTO.setCompletedOn(LocalDate.now());
        }
        return toDoMapper.toDto(toDoRepository.save(toDoEntity));
    }

    @Override
    public boolean deleteToDo(final Integer id) {
        log.info("Deleting ToDo...");
        if (!toDoRepository.existsById(id)) {
            log.warn("ToDo with id `{}` does not exist", id);
            return false;
        }
        log.debug("ToDo exists with id `{}`", id);
        toDoRepository.deleteById(id);
        log.info("ToDo removed successfully");
        return true;
    }

    private void validateData(final ToDoDto toDoDTO) {
        log.info("Validating ToDoDTO...");
        if (Objects.isNull(toDoDTO)) {
            log.error("ToDoDTO is null");
            throw new NullPointerException("There is no data to add");
        }
        if (StringUtils.hasText(toDoDTO.getDescription())) {
            log.error("Description is empty");
            throw new NullPointerException("There is no description provided");
        }
        log.info("ToDoDTO is valid");
    }
}
