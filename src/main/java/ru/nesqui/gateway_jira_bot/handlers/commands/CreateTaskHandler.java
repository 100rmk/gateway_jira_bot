package ru.nesqui.gateway_jira_bot.handlers.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.nesqui.gateway_jira_bot.handlers.BotState;
import ru.nesqui.gateway_jira_bot.handlers.InputMessageHandler;
import ru.nesqui.gateway_jira_bot.services.VoiceConvertService;

@Component
public class CreateTaskHandler implements InputMessageHandler {
    private VoiceConvertService voiceConvertService;

    public CreateTaskHandler(VoiceConvertService voiceConvertService) {
        this.voiceConvertService = voiceConvertService;
    }

    @Override
    public SendMessage handle(Message message) {
        SendMessage sendMessage;

        if (message.hasText()) {
            sendMessage = new SendMessage().setChatId(message.getChatId()).setText(message.getText());
        } else if (message.hasVoice()) {
            try {
                sendMessage = new SendMessage()
                        .setChatId(message.getChatId())
                        .setText(voiceConvertService.getTextFromVoice(message));
            } catch (Exception e) {
                sendMessage = new SendMessage().setChatId(message.getChatId()).setText("Voice error");
                e.printStackTrace();
            }
        } else {
            sendMessage = new SendMessage().setChatId(message.getChatId()).setText("Empty message");
        }

        return sendMessage;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.CREATE_TASK;
    }
}
