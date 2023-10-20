package com.microsoft.aoai.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.azure.ai.openai.models.ChatMessage;
import com.azure.ai.openai.models.ChatRole;
import com.microsoft.aoai.demo.model.Settings;
import com.microsoft.aoai.demo.repository.GetMessageRepository;

@Service
public class GetMessageService {

    Logger logger = Logger.getLogger(GetMessageService.class.getName());
    private GetMessageRepository getMessageRepository;
    private Settings settings;

    public GetMessageService(GetMessageRepository getMessageRepository, Settings settings) {

        try {
            if (settings == null || settings.getOpenaiSystemPrompt() == null) {
                throw new Exception("環境変数が設定されていません。");
            }

            this.settings = settings;

        } catch (Exception e) {
            logger.severe(e.getMessage());
        }

        this.getMessageRepository = getMessageRepository;
    }

    public List<String> getMessage(List<String> message) {
        return getMessageRepository.getMessage(buildPrompt(message));
    }

    private List<ChatMessage> buildPrompt(List<String> userInput) {
        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(new ChatMessage(ChatRole.SYSTEM, this.settings.getOpenaiSystemPrompt()));
        userInput.forEach(
                input -> chatMessages.add(new ChatMessage(ChatRole.USER, input)));
        return chatMessages;
    }

}
