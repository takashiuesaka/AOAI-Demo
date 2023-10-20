package com.microsoft.aoai.demo.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
public class Settings {

    @Getter
    @Value("${OPENAI_ENDPOINT}")
    private String openaiEndpoint;

    @Getter
    @Value("${OPENAI_KEY}")
    private String openaiKey;

    @Getter
    @Value("${OPENAI_MODEL_NAME}")
    private String openaiModelName;

    @Getter
    @Value("${OPENAI_SYSTEM_PROMPT}")
    private String openaiSystemPrompt;

    @Value("${COMPUTER_VISION_ENDPOINT}")
    private String computerVisionEndpoint;

    @Getter
    @Value("${COMPUTER_VISION_KEY}")
    private String computerVisionKey;

    @Getter
    @Value("${PORT}")
    private int port;

    @Getter
    @Value("${tmp.dir}")
    private String TmpDir;

    public String getComputerVisionEndpoint() {
        if (this.computerVisionEndpoint != null && !this.computerVisionEndpoint.endsWith("/")) {
            return this.computerVisionEndpoint += "/";
        }

        return this.computerVisionEndpoint;
    }
}
