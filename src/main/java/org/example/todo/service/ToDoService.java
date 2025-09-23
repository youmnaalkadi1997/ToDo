package org.example.todo.service;


import org.example.todo.model.ToDo;
import org.example.todo.model.ToDoDTO;
import org.example.todo.repository.ToDoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class ToDoService {

    private final ToDoRepository toDoRepository;
    private ChatGPTService chatGPTService;

    @Lazy
    @Autowired
    public void setChatGPTService(ChatGPTService chatGPTService) {
        this.chatGPTService = chatGPTService;
    }

    public ToDoService(ToDoRepository toDoRepository ) {
        this.toDoRepository = toDoRepository;
    }




    public List<ToDo> getAllRequests() {
        return toDoRepository.findAll();
    }

    public ToDo addToDo(ToDoDTO toDoDto) {
        String id = UUID.randomUUID().toString();
        String newDescription = chatGPTService.checkSpelling(toDoDto.getDescription());
        ToDo toDo = ToDo.builder().id(id).description(newDescription).status(toDoDto.getStatus()).build();
        return toDoRepository.save(toDo);
    }

    public Optional<ToDo> getToDoById(String id) {
        return toDoRepository.findById(id);
    }

    public ToDo updateToDo(String id, ToDoDTO toDoDTO) {
        Optional<ToDo> existingToDoOpt = toDoRepository.findById(id);
        if (existingToDoOpt.isPresent()) {
            ToDo existingToDo = existingToDoOpt.get();
            existingToDo.setDescription(toDoDTO.getDescription());
            existingToDo.setStatus(toDoDTO.getStatus());

            return toDoRepository.save(existingToDo);
        } else {
            return null;
        }
    }

    public void deleteToDoById(String id) {
        if (toDoRepository.existsById(id)) {
            toDoRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("No ToDo found with Id:" + id);
        }
    }
}
