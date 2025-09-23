package org.example.todo.service;


import org.example.todo.model.openAIRequest;
import org.example.todo.model.openALResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


@Service
public class ChatGPTService {

    private final RestClient restClient;
    public ChatGPTService(RestClient.Builder restClientBuilder ,@Value("${API_KEY}") String apikey) {
        this.restClient = restClientBuilder
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader("Authorization", "Bearer " + apikey)
                .build();

    }

    public String checkSpelling(String  description) {
        String message = "Korrigiere diesen Text auf Rechtschreibfehler: \"" + description + "\"";
        openAIRequest request = openAIRequest.builder().model("gpt-3.5-turbo-instruct").prompt(message).max_tokens(7)
                .temperature(0).build();

        openALResponse response = restClient.post()
                .uri("/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(openALResponse.class);

       return response.choices().get(0).text().trim();
    }

}
