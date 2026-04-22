package com.sxxian.marketingagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.List;

/**
 * 向量数据库配置，基于内存
 */
@Configuration
public class MarketingAppVectorStoreConfig {

    @Resource
    private MarketingAppDocumentLoader marketingAppDocumentLoader;

    @Resource MyKeywordEnricher myKeywordEnricher;

    /**
     * 加载markdown文档，实现初始化向量库并保存文档
     * @param dashscopeEmbeddingModel
     * @return
     */
    @Bean
    VectorStore marketingAppVectorStore(EmbeddingModel dashscopeEmbeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel)
                .build();
        // 加载文档
        List<Document> documents = marketingAppDocumentLoader.loadMarkdowns();
        // 自动补充关键词元信息
        List<Document> enrichedDocuments = myKeywordEnricher.enrichDocuments(documents);
        simpleVectorStore.add(enrichedDocuments);
        return simpleVectorStore;
    }

}
