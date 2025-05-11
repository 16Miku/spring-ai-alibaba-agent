package com.miku.springaialibabaagent;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestClient;

import java.util.List;

@SpringBootApplication
@MapperScan("com.miku.springaialibabaagent.mapper") // 指定Mapper接口所在的包
@Slf4j
public class SpringAiAlibabaAgentApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAiAlibabaAgentApplication.class, args);
    }



    @Bean
    CommandLineRunner ingestTermsOfServiceToVectorStore(EmbeddingModel embeddingModel, VectorStore vectorStore,
        @Value("classpath:rag/terms-of-service.txt") Resource termsOfServiceDocs ) {

        return args -> {

            log.info("将服务条款文档（terms-of-service.txt）加载到向量存储中" );


            TextReader textReader = new TextReader( termsOfServiceDocs );

            List<Document> documentText = textReader.read();


            TokenTextSplitter tokenTextSplitter = new TokenTextSplitter();

            var splitDocuments = tokenTextSplitter.transform(documentText);


            vectorStore.write(splitDocuments);

            log.info("'terms-of-service.txt' 摄取成功");


            log.info(" 在文档摄取后，执行一次相似性搜索，以验证数据是否已成功加载并可以被检索");

            vectorStore.similaritySearch("Miku").forEach(
                    document -> {
                        log.info("找到的相似文档的文本内容:{}",document.getText());
                    }
            );


        };


    }


    @Bean
    public VectorStore vectorStore( EmbeddingModel embeddingModel ) {

        log.info("Creating SimpleVectorStore Bean...");

        return SimpleVectorStore.builder(embeddingModel).build();

    }


    @Bean
    public ChatMemory chatMemory() {

        log.info("Creating InMemoryChatMemory Bean...");

        return new InMemoryChatMemory();

    }

    @Bean
    @ConditionalOnMissingBean
    public RestClient.Builder restClientBuilder() {

        log.info("Creating RestClient.Builder Bean (if not already present)...");

        return RestClient.builder();

    }

}
