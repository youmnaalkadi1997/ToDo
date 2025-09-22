package org.example.todo.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ToDoDTO {

    @NotBlank(message = "Description darf nicht leer sein")
    public String description;
    public Status status;
}
//41105b29-f7be-47d1-95e4-854b85fe6bd7