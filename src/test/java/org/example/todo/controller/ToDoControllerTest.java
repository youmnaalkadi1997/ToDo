package org.example.todo.controller;

import org.example.todo.model.Status;
import org.example.todo.model.ToDo;
import org.example.todo.repository.ToDoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest
@AutoConfigureMockMvc
class ToDoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ToDoRepository toDoRepository;

    @Test
    void getAllToDoReq() throws Exception {

        ToDo todoList = ToDo.builder().id("1").description("Testing").status(Status.OPEN).build();
        toDoRepository.save(todoList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                   [
                                   {
                                     "id" : "1",
                                    "description" : "Testing",
                                    "status" : "OPEN"
                                   }
                                   ]
                                   """
                ));
    }

    @Test
    void addToDo() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                  {
                                    "id" : "1",
                                    "description" : "Testing",
                                    "status" : "OPEN"
                                   }
                                 """)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                   {
                                    "id" : "1",
                                    "description" : "Testing",
                                    "status" : "OPEN"
                                   }
                               
                                   """
                ));
    }

    @Test
    void getToDoById() throws Exception {

        ToDo todoList = ToDo.builder().id("1").description("Testing").status(Status.OPEN).build();
        toDoRepository.save(todoList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/{id}", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                 
                                   {
                                     "id" : "1",
                                    "description" : "Testing",
                                    "status" : "OPEN"
                                   }
                                   
                                   """
                ));
    }

    @Test
    void updateToDo() throws Exception {

        ToDo todoList = ToDo.builder().id("1").description("Testing").status(Status.OPEN).build();
        toDoRepository.save(todoList);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/todo/{id}" , "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                  {
                                    "description" : "Testing",
                                    "status" : "IN_PROGRESS"
                                   }
                                 """)
                        ).andExpect(MockMvcResultMatchers.status().isOk())
                         .andExpect(MockMvcResultMatchers.content().json(
                                 """
                                          
                                            {
                                              "id" : "1",
                                             "description" : "Testing",
                                             "status" : "IN_PROGRESS"
                                            }
                                            
                                            """
                         ));
    }

    @Test
    void deleteToDo() throws Exception {

        ToDo todoList = ToDo.builder().id("1").description("Testing").status(Status.OPEN).build();
        toDoRepository.save(todoList);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/todo/{id}", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}