package org.example.todo.controller;

import org.example.todo.model.ToDo;
import org.example.todo.model.ToDoDTO;
import org.example.todo.service.ToDoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
        return  toDoService.getAllRequests();
    }

    @PostMapping("/todo")
    public ToDo addToDo(@RequestBody ToDo toDo)  {
        return  toDoService.addToDo(toDo);
    }

    @GetMapping("/todo/{id}")
    public ToDo getToDoById(@PathVariable String id) {

        Optional<ToDo> toDotOpt =  toDoService.getToDoById(id);
        if(toDotOpt.isPresent()){
            return toDotOpt.get();
        }
        throw new RuntimeException("ToDo mit ID: " + id + " nicht verfügbar");
    }

    @PutMapping("/todo/{id}")
    public ToDo updateToDo(@PathVariable String id, @RequestBody ToDoDTO toDoDTO) {
        ToDo updatedToDo = toDoService.updateToDo(id, toDoDTO);
        if (updatedToDo != null) {
            return updatedToDo;
        } else {
            throw new RuntimeException("ToDo mit ID: " + id + " nicht verfügbar");
        }
    }

    @DeleteMapping("/todo/{id}")
    public void deleteToDo(@PathVariable String id){
        toDoService.deleteToDoById(id);
    }
}
