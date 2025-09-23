package org.example.todo.controller;

import org.example.todo.model.Status;
import org.example.todo.model.ToDo;
import org.example.todo.repository.ToDoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureMockRestServiceServer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureMockRestServiceServer
class ToDoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ToDoRepository toDoRepository;

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

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

        mockRestServiceServer.expect(requestTo("https://api.openai.com/v1/completions"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Authorization", "Bearer test-api-key"))
                .andExpect(content().json("""
                        {
                              "model": "gpt-3.5-turbo-instruct",
                              "prompt": "Korrigiere diesen Text auf Rechtschreibfehler: \\"is this line god or no\\"",
                              "max_tokens": 7,
                              "temperature": 0             
                          }
                """ ))
                .andRespond(withSuccess(
                        """
                                {
                                   "choices": [
                                     {
                                       "text": "Is this line good or not",
                                       "index": 0
                                     }
                                   ]
                                 }
                                """ ,MediaType.APPLICATION_JSON
                ));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/todo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                  {
                                    "description" : "is this line god or no",
                                    "status" : "OPEN"
                                   }
                                 """)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        """
                                   {
                                    "description" : "Is this line good or not",
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