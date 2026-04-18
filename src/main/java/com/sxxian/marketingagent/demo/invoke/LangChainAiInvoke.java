package com.sxxian.marketingagent.demo.invoke;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.chat.ChatModel;

public class LangChainAiInvoke {

    public static void main(String[] args) {
        ChatModel qwenModel = QwenChatModel.builder()
                .apiKey(TestApiKey.API_KEY)
                .modelName("qwen-plus")
                .build();
        String answer = qwenModel.chat("你好，你是哪一个模型？告诉我具体的模型Code。");
        System.out.println(answer);
    }
}
