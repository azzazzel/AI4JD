package com.milendyankov.demos.ai4jd;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;

public class ModelEmbeddings {

    static EmbeddingModel getModel() {
        return new AllMiniLmL6V2EmbeddingModel();
    }

    static Embedding generateEmbedding(String text) {
        return getModel().embed(text).content();
    }

    static Embedding generateEmbedding(TextSegment text) {
        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
        return embeddingModel.embed(text).content();
    }

    static float[] embed(String text) {
        return generateEmbedding(text).vector();
    }

    static int getModelDimentions() {
        return generateEmbedding("tmp").dimension();
    }

}


