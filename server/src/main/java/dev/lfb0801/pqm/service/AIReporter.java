package dev.lfb0801.pqm.service;

import java.util.Map;
import java.util.Set;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lfb0801.pqm.domain.Aggregate;
import dev.lfb0801.pqm.domain.Comparing;

@Service
@ConditionalOnBean(ChatClient.class)
public class AIReporter {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AIReporter(ChatClient chatClient) {
        this.chatClient = chatClient;
    }


    public String report(Comparing<Map.Entry<String, Set<Aggregate>>> comparing) throws JsonProcessingException {

        var message = """
            We wan't to compare 2 versions of a project.
            It's your task to look at the given information about the 2 versions and provide a report about the differences.
            Focussing on how the health of the project is affected by the changes, from version 1 to version 2.
            
            version 1 = %s:
            %s
            
            version 2 = %s:
            %s
            """.formatted(comparing.left()
                .getKey(),
            objectMapper.writeValueAsString(comparing.left()
                .getValue()),
            comparing.right()
                .getKey(),
            objectMapper.writeValueAsString(comparing.right()
                .getValue())
        );

        return chatClient.prompt(new Prompt(message))
            .call()
            .content();
    }
}
