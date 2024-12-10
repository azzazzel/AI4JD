package com.milendyankov.demos.ai4jd;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.rag.content.Content;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;

import java.util.List;

public class _14_RAG {
    public static void main(String[] args) {

        String query = "how to play a song on a piano?";

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

        List<Content> retrieved = contentRetriever.retrieve(new Query("how to play a song on a piano?"));
        retrieved.forEach(System.out::println);

        Librarian librarian = AiServices.builder(Librarian.class)
                .chatLanguageModel(model)
                .contentRetriever(contentRetriever)
                .build();

        String answer = librarian.chat(query);
        System.out.println(answer);
    }

    interface Librarian {
        @SystemMessage({
                "You are a librarian.",
                "Divide your response into two sections",
                "For each section respond with the names and a brief summary of books related to the question",
                "First section is 'books we have' and must only contain books provided in the context of the request",
                "Second section is 'other books' and should contains other books you may know of ",
                "The titles of the books in the context are under the 'title' key in the metadata",
                "Don't ask questions or try to interact. Respond only with the names and summaries"
        })
        String chat(String userMessage);
    }
}
