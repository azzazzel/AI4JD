package com.milendyankov.demos.ai4jd;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.rag.DefaultRetrievalAugmentor;
import dev.langchain4j.rag.RetrievalAugmentor;
import dev.langchain4j.rag.content.injector.ContentInjector;
import dev.langchain4j.rag.content.injector.DefaultContentInjector;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;

import java.util.List;

public class _14_RAG {
    public static void main(String[] args) {

        String query = "I want to learn to play a song on a piano";

        ChatLanguageModel model =
                OllamaChatModel.builder()
                        .baseUrl("http://localhost:11434")
                        .modelName("gemma3")
                        .build();


        EmbeddingStore<TextSegment> embeddingStore = PgVectorEmbeddingStore.builder()
                .host(PostgressDB.HOST)
                .port(PostgressDB.PORT)
                .database(PostgressDB.DB)
                .user(PostgressDB.USER)
                .password(PostgressDB.PASSWORD)
                .table("embeddings")
                .dimension(ModelEmbeddings.getModelDimentions())
                .build();

        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(ModelEmbeddings.getModel())
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
                "You are a helpful librarian helping a person finding relevant books.",
                "Requests will contain information about what customer wants",
                "Requests will have attached a JSON formated list of books available in the store.",
                "Respond in natural human language with a few book titles that would help the customer. Explain briefly why.",
                "First mention the books available in the store.",
                "Then you may mention other books you know of that the store does not have.",
                "Be concise and to the point. Don't ask questions or try to interact. ",
        })
        String chat(String userMessage);
    }
}
