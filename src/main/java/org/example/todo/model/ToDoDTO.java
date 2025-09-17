package org.example.todo.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ToDoDTO {

    public String description;
    public Status status;
}
