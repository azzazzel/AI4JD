package com.milendyankov.demos.ai4jd;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pinecone.PineconeEmbeddingStore;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;

public class _10_SearchVectorStore {
    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.load();

        EmbeddingStore<TextSegment> embeddingStore = PineconeEmbeddingStore.builder()
                .apiKey(dotenv.get("PINECONE_API_KEY"))
                .index("ai4jd")
                .build();


        String query = "I want to learn to play a song on a piano";

        Embedding queryEmbedding = ModelEmbeddings.generateEmbedding(query);

        EmbeddingSearchRequest embeddingSearchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(5)
                .build();

        List<EmbeddingMatch<TextSegment>> relevant = embeddingStore.search(embeddingSearchRequest).matches();

        System.out.println("❓" + query);
        for (EmbeddingMatch<TextSegment> embeddingMatch : relevant) {
            TextSegment textSegment = embeddingMatch.embedded();
            System.out.println("------ (" + embeddingMatch.score() + ")");
            System.out.println("\t\uD83D\uDCD7" + textSegment.metadata().getString("title"));
            System.out.println("\t" + textSegment.text());

        }
    }
}
