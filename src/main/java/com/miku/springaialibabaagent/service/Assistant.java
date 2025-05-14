package com.miku.springaialibabaagent.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;


// 静态导入ChatMemoryAdvisor中的常量，用于设置聊天记忆的参数
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Service
public class Assistant {

    private final ChatClient chatClient;




    public Assistant(ChatClient.Builder builder, VectorStore vectorStore, ChatMemory chatMemory, ToolCallbackProvider tools) {

        this.chatClient = builder
                .defaultSystem("""
                        你是一名AI客服，你的任务是和蔼地回答用户的问题，并通过函数调用执行查询、修改、新建、删除等各种业务。
                        """)
                .defaultAdvisors(
                        new PromptChatMemoryAdvisor(chatMemory),
                        new QuestionAnswerAdvisor(vectorStore, SearchRequest.builder().topK(4).similarityThresholdAll().build()),
                        new SimpleLoggerAdvisor()
                )
                .defaultTools("getItemById",
                        "getOrderById","createOrder","updateOrderStatus",
                        "getUserById","createUser","updateUser",
                        "createPayment","getPaymentByOrderId","updatePaymentStatus")
                .defaultTools(tools)
                .build();



    }



    public Flux<String> stream( String chatId , String message ) {

        return this.chatClient
                .prompt()
                .user(message)
                .advisors(
                        advisorSpec -> advisorSpec
                                .param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                                .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100)
                )
                .stream()
                .content();
    }


    public String chat( String chatId , String message ) {

        return this.chatClient
                .prompt()
                .user(message)
                .advisors(
                        advisorSpec -> advisorSpec
                                .param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                                .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100)
                )
                .call()
                .content();
    }


}
