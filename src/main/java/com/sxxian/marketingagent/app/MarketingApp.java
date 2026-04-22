package com.sxxian.marketingagent.app;


import com.sxxian.marketingagent.advisor.MyLoggerAdvisor;
import com.sxxian.marketingagent.chatmemory.FileBasedChatMemory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.MessageAggregator;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

@Component  //组件注解
@Slf4j //打日志
public class MarketingApp {


    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = """
            你是一名资深电商营销专家，熟悉淘宝、抖音、小红书、Amazon 等平台的运营与增长策略，擅长通过数据分析和用户洞察提升商品转化率与销售额。
            你正在与用户进行一对一的营销咨询，对话目标是：深入理解用户的产品与经营情况，并提供可执行的营销策略与优化建议。
            请遵循以下原则：
            1. 优先提问：当信息不足时，通过关键问题（产品、价格、目标人群、渠道、数据表现等）逐步获取完整背景，而不是直接下结论。
            2. 分析问题：从“曝光 → 点击 → 转化”的漏斗出发，判断问题所在，并给出清晰原因。
            3. 输出建议：提供具体、可执行的优化方案（如文案优化、活动设计、定价策略、转化提升手段等），避免空泛表述。
            4. 多轮引导：结合上下文持续追问与深入分析，模拟真实营销顾问的咨询过程。
            5. 转化导向：所有建议以提升点击率（CTR）和转化率（CVR）为核心目标。
            你的角色不是简单回答问题，而是帮助用户做出更有效营销决策的增长顾问。回答过程中需要体现出你的专业性，更加简短有力。
          """;

    /**
     * 初始化ChatClient
     * @param dashscopeChatModel
     */

    public MarketingApp(ChatModel dashscopeChatModel) {
        //基于文件
        String fileDir = System.getProperty("user.dir")+"/tmp/chat-memory";
        ChatMemory chatMemory = new FileBasedChatMemory(fileDir);

        //初始化基于内存的对话记忆
//        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.builder()
//                .chatMemoryRepository(new InMemoryChatMemoryRepository())
//                .maxMessages(20)
//                .build();
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                        // 自定义日志 Advisor，可按需开启
                        ,new MyLoggerAdvisor()
                )
                .build();
    }

    /**
     * AI基础对话，多轮对话记忆
     * @param message
     * @param chatId
     * @return
     */
    public String doChat(String message, String chatId){
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content:{}",content);
        return content;
    }

    record MarketingReport(String title, List<String> suggestions){ }


    /**
     * 结构化输出报告
     * @param message
     * @param chatId
     * @return
     */
    public MarketingReport doChatWithReport(String message, String chatId){
        MarketingReport marketingReport = chatClient
                .prompt()
                .system(SYSTEM_PROMPT+"每次对话都生成营销策略报告，标题为{产品名}的营销策略报告，内容为营销方案列表。")
                .user(message)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .entity(MarketingReport.class);
        log.info("marketingReport{}",marketingReport);
        return marketingReport;
    }


    // AI 恋爱知识库问答功能

    @Resource
    private VectorStore marketingAppVectorStore;

    @Resource
    private Advisor marketingAppRagCloudAdvisor;


    /**
     * RAG
     * @param message
     * @param chatId
     * @return
     */
    public String doChatWithRag(String message, String chatId){
        ChatResponse chatResponse = chatClient
                .prompt()
                .user(message)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
//                .advisors(new QuestionAnswerAdvisor(marketingAppVectorStore))
                // 应用 RAG 检索增强服务（基于云知识库服务）
                .advisors(marketingAppRagCloudAdvisor)
                .call()
                .chatResponse();
        String content = chatResponse.getResult().getOutput().getText();
        log.info("RagContent{}",content);
        return content;
    }
}
