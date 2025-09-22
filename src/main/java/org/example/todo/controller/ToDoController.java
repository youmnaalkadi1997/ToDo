package org.example.todo.controller;

import jakarta.validation.Valid;
import org.example.todo.model.ToDo;
import org.example.todo.model.ToDoDTO;
import org.example.todo.service.ToDoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class ToDoController {

    private final ToDoService toDoService;
    public ToDoController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @GetMapping("/todo")
    public List<ToDo> getAllToDoReq() {

        if(!toDoService.getAllRequests().isEmpty()){
            return toDoService.getAllRequests();
        }
        else
            throw new NoSuchElementException("Keine ToDo liste gefunden");
    }

    @PostMapping("/todo")
    public ToDo addToDo(@Valid @RequestBody ToDoDTO toDoDto)  {
        return  toDoService.addToDo(toDoDto);
    }

    @GetMapping("/todo/{id}")
    public ToDo getToDoById(@PathVariable String id) {

        Optional<ToDo> toDotOpt =  toDoService.getToDoById(id);
        if(toDotOpt.isPresent()){
            return toDotOpt.get();
        }
        throw new NoSuchElementException("ToDo mit ID: " + id + " nicht verfügbar");
    }

    @PutMapping("/todo/{id}")
    public ToDo updateToDo(@PathVariable String id, @RequestBody ToDoDTO toDoDTO) {
        ToDo updatedToDo = toDoService.updateToDo(id, toDoDTO);
        if (updatedToDo != null) {
            return updatedToDo;
        } else {
            throw new NoSuchElementException("ToDo mit ID: " + id + " nicht verfügbar");
        }
    }

    @DeleteMapping("/todo/{id}")
    public void deleteToDo(@PathVariable String id){
        toDoService.deleteToDoById(id);
    }
}
