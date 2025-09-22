package org.example.todo.exception;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorMessage(String message , int status , LocalDateTime timestamp) {
}
