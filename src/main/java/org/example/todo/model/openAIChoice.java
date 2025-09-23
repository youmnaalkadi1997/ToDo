package org.example.todo.model;

import lombok.Builder;
import lombok.With;

@Builder
@With
public record openAIChoice(String text , int index) {
}
