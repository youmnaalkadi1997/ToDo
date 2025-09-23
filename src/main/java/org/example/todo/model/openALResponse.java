package org.example.todo.model;

import lombok.Builder;
import lombok.With;

import java.util.List;

@Builder
@With
public record openALResponse(List<openAIChoice> choices) {
}
