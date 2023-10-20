package com.microsoft.aoai.demo.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Repository;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.ChatCompletions;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatMessage;
import com.azure.core.credential.AzureKeyCredential;
import com.microsoft.aoai.demo.model.Settings;

@Repository
public class GetMessageRepository {

    private OpenAIClient openAIClient;
    private Settings settings;
    Logger logger = Logger.getLogger(GetMessageRepository.class.getName());

    public GetMessageRepository(Settings settings) throws Exception {

        try {
            if (settings == null || settings.getOpenaiEndpoint() == null || settings.getOpenaiKey() == null
                    || settings.getOpenaiModelName() == null) {
                throw new Exception("環境変数が設定されていません。");
            }

            this.settings = settings;

        } catch (Exception e) {
            logger.severe(e.getMessage());
            throw e;
        }

        // SDK Version 1.0.0-beta.4 では以下のエラーが発生
        // The type com.azure.core.credential.KeyCredential cannot be resolved. It is
        // indirectly referenced from required type
        // com.azure.ai.openai.OpenAIClientBuilder
        this.openAIClient = new OpenAIClientBuilder()
                .credential(new AzureKeyCredential(this.settings.getOpenaiKey()))
                .endpoint(this.settings.getOpenaiEndpoint())
                .buildClient();
    }

    public List<String> getMessage(List<ChatMessage> message) {
        List<String> chatResult = new ArrayList<String>();
        ChatCompletions chatCompletions = this.openAIClient.getChatCompletions(this.settings.getOpenaiModelName(),
                new ChatCompletionsOptions(message));
        chatCompletions.getChoices().forEach(choice -> {
            ChatMessage chatMessage = choice.getMessage();
            String content = chatMessage.getContent();
            chatResult.add(content);
        });
        return chatResult;
    }
}
