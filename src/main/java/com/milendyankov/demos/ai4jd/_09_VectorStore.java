package com.milendyankov.demos.ai4jd;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class _09_VectorStore {
    public static void main(String[] args) throws SQLException {
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

        Connection conn = PostgressDB.getConnection();
        PreparedStatement selectStmt = conn.prepareStatement("SELECT title, description FROM books_embeddings");
        ResultSet resultSet = selectStmt.executeQuery();
        while (resultSet.next()) {
            String title = resultSet.getString("title");
            String description = resultSet.getString("description");
            if (description != null) {
                Metadata metadata = Metadata.metadata("title", title);
                TextSegment textSegment = TextSegment.from(description, metadata);
                Embedding embedding = embeddingModel.embed(textSegment).content();
                embeddingStore.add(embedding, textSegment);
            }
        }
        System.out.println("Vector store update complete.");
    }
}
