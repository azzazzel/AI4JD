package com.milendyankov.demos.ai4jd;

import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pinecone.PineconeEmbeddingStore;
import dev.langchain4j.store.embedding.pinecone.PineconeServerlessIndexConfig;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class _09_VectorStore {
    public static void main(String[] args) throws SQLException {

        Dotenv dotenv = Dotenv.load();

        EmbeddingStore<TextSegment> embeddingStore = PineconeEmbeddingStore.builder()
                .apiKey(dotenv.get("PINECONE_API_KEY"))
                .index("ai4jd")
                .createIndex(PineconeServerlessIndexConfig.builder()
                        .cloud("AWS")
                        .region("us-east-1")
                        .dimension(ModelEmbeddings.getModelDimentions())
                        .build())
                .build();


        Connection conn = PostgressDB.getConnection();
        PreparedStatement selectStmt = conn.prepareStatement("SELECT title, description FROM books");
        ResultSet resultSet = selectStmt.executeQuery();
        List<Embedding> embeddings = new LinkedList<>();
        List<TextSegment> textSegments = new LinkedList<>();

        while (resultSet.next()) {
            String title = resultSet.getString("title");
            String description = resultSet.getString("description");
            if (description != null) {
                Metadata metadata = Metadata.metadata("title", title);
                TextSegment textSegment = TextSegment.from(description, metadata);
                Embedding embedding = ModelEmbeddings.generateEmbedding(textSegment);
                embeddings.add(embedding);
                textSegments.add(textSegment);
            }
            if (embeddings.size() == 500) {
                System.out.println("Upserting 500 vectors ...");
                embeddingStore.addAll(embeddings, textSegments);
                embeddings.clear();
                textSegments.clear();
            }
        }

        System.out.println("Upserting last " + embeddings.size() + " vectors .");
        embeddingStore.addAll(embeddings, textSegments);

        System.out.println("Vector store update complete.");
    }
}
