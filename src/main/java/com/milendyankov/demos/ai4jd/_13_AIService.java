package com.milendyankov.demos.ai4jd;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;

public class _13_AIService {
    public static void main(String[] args) {

        String query = "how to play a song on a piano?";

        ChatLanguageModel model =
                OllamaChatModel.builder()
                        .baseUrl("http://localhost:11434")
                        .modelName("llama3.2")
                        .build();

        Librarian librarian = AiServices.builder(Librarian.class)
                .chatLanguageModel(model)
                .build();

        String answer = librarian.chat(query);
        System.out.println(answer);
    }

    interface Librarian {
        @SystemMessage({
                "You are a librarian.",
                "Respond with the names and a brief summary of books related to the question",
                "Limit your response to maximum 3 books",
                "Don't ask questions or try to interact. Respond only with the names and summaries"
        })
        String chat(String userMessage);
    }
}
