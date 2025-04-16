package com.milendyankov.demos.ai4jd;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;

import java.util.List;

public class _10_SearchVectorStore {
    public static void main(String[] args) {

        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();

        EmbeddingStore<TextSegment> embeddingStore = PgVectorEmbeddingStore.builder()
                .host(PostgressDB.HOST)
                .port(PostgressDB.PORT)
                .database(PostgressDB.DB)
                .user(PostgressDB.USER)
                .password(PostgressDB.PASSWORD)
                .table("embeddings")
                .dimension(embeddingModel.embed("tmp").content().dimension())
                .build();

        String query = "I want to learn to play a song on a piano";

        Embedding queryEmbedding = embeddingModel.embed(query).content();

        EmbeddingSearchRequest embeddingSearchRequest = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(5)
                .build();

        List<EmbeddingMatch<TextSegment>> relevant = embeddingStore.search(embeddingSearchRequest).matches();

        for (EmbeddingMatch embeddingMatch : relevant) {
            TextSegment textSegment = (TextSegment) embeddingMatch.embedded();
            System.out.println("---");
            System.out.println(textSegment.metadata().getString("title"));
            System.out.println("---");
            System.out.println("\t" + textSegment.text());
        }

        EmbeddingMatch<TextSegment> embeddingMatch = relevant.get(0);
    }
}
