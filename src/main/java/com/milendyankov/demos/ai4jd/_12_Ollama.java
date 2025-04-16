package com.milendyankov.demos.ai4jd;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

public class _12_Ollama {
    public static void main(String[] args) {

        String query = "I want to learn to play a song on a piano";

        String modelName = "llama3.2";

        ChatLanguageModel model =
                OllamaChatModel.builder()
                        .baseUrl("http://localhost:11434")
                        .modelName(modelName)
                        .build();

        String answer = model.generate(query);
        System.out.println(answer);
    }
}
