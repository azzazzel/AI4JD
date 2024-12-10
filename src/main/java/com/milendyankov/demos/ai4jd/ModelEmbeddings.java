package com.milendyankov.demos.ai4jd;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;

public class ModelEmbeddings {
    static float[] generateEmbedding(String text) {
        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
        Embedding embedding = embeddingModel.embed(text).content();
        return embedding.vector();
    }
}


