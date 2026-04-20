## 开发日志

### 阶段性任务
1. 初始化项目（已完成）

   依赖包、通用代码

2. 调用AI模型
    SDK、HTTP、Spring AI 调用大模型。

    使用dashscope 阿里百炼，灵积
     
```
    {"requestId":"07ece7e7-4e4e-9472-a7d4-b70d44076ed1",
     "usage":{"input_tokens":22,"output_tokens":66,"total_tokens":88,"prompt_tokens_details":{"cached_tokens":0}},
     "output":{"choices":[{"finish_reason":"stop",
        "message":{"role":"assistant",
                    "content":"你好！我是通义千问（Qwen），阿里巴巴集团旗下的超大规模语言模型。我能够回答问题、创作文字，比如写故事、写公文、写邮件、写剧本、逻辑推理、编程等等，还能表达观点，玩游戏等。如果你有任何问题或需要帮助，欢迎随时告诉我！😊"}}]},
     "status_code":200,"code":"","message":""}
```


3. 多轮对话

    完成多轮对话。

    基于文件实现会话持久化。（难点在于对象和文件的序列化和反序列化）


4. RAG



4. MCP