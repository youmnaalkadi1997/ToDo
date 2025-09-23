package org.example.todo.model;

import lombok.Builder;
import lombok.With;

@With
@Builder
public record openAIRequest(String model , String prompt , int max_tokens , int temperature ) {
}
