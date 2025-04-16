package com.milendyankov.demos.ai4jd;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.injector.ContentInjector;
import dev.langchain4j.rag.content.injector.DefaultContentInjector;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;

import java.util.Arrays;
import java.util.List;

public class _14_RAG {
    public static void main(String[] args) {

        String query = "I want to learn to play a song on a piano";

        ChatLanguageModel model =
                OllamaChatModel.builder()
                        .baseUrl("http://localhost:11434")
                        .modelName("llama3.2")
                        .build();

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

        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(5)
                .build();

        ContentInjector contentInjector = DefaultContentInjector.builder()
                .metadataKeysToInclude(List.of("title"))
                .build();

        RetrievalAugmentor retrievalAugmentor = DefaultRetrievalAugmentor.builder()
                .contentRetriever(contentRetriever)
                .contentInjector(contentInjector)
                .build();

        Librarian librarian = AiServices.builder(Librarian.class)
                .chatLanguageModel(model)
                .retrievalAugmentor(retrievalAugmentor)
                .build();

        String answer = librarian.chat(query);
        System.out.println("\n\n ====== ");
        System.out.println(answer);
    }

    interface Librarian {
        @SystemMessage({
                "The user is providing information about what they want to learn.",
                "You are a helpful librarian helping then find the relevant books.",
                "Respond as a human in real bookstore would",
                "Provide the titles and briefly explain how it helps",
                "First mention all relevant available books. They must be from the bookstore DB provided in the request.",
                "Then you may add other books (not found in the request) than can help with the request.",
                "The titles of the books in the context are under the 'title' key in the metadata",
                "Be concise and to the point. Don't ask questions or try to interact."
        })
        String chat(String userMessage);
    }
}
