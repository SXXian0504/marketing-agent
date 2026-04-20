package com.sxxian.marketingagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MarketingAppTest {

    @Resource
    private MarketingApp marketingApp;

    @Test
    void doChat() {
        String chatId = UUID.randomUUID().toString();
        String message = "你好，我是负责小红书发布图文营销文案推广产品的小X。每次回答在20字以内。";
        String answer = marketingApp.doChat(message, chatId);

        message = "我最近需要卖一款可爱手绘风格的保温杯";
        answer = marketingApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);

        message = "你还记得我的名字和我需要卖的产品吗";
        answer = marketingApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithReport() {
        String chatId = UUID.randomUUID().toString();
        String message = "你好，我是负责小红书发布图文营销文案推广产品的小X。我现在需要营销推广一个手绘风格保温杯，但我不知道怎么做。";
        MarketingApp.MarketingReport marketingReport = marketingApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(marketingReport);
    }
}