package com.milendyankov.demos.ai4jd;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.jlama.JlamaChatModel;

public class _11_Jlama {
    public static void main(String[] args) {

        String query = "I want to learn to play a song on a piano";

        ChatLanguageModel model = JlamaChatModel.builder()
                .modelName("tjake/TinyLlama-1.1B-Chat-v1.0-Jlama-Q4")
                .build();

        String answer = model.generate(query);
        System.out.println(answer);
    }
}
