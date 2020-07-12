package ru.nesqui.gateway_jira_bot.handlers.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.nesqui.gateway_jira_bot.handlers.BotState;
import ru.nesqui.gateway_jira_bot.handlers.InputMessageHandler;

@Component
public class StartHandler implements InputMessageHandler {

    @Override
    public SendMessage handle(Message message) {
            return new SendMessage()
                    .setChatId(message.getChatId())
                    .setText("Вы авторизовались");
    }

    @Override
    public BotState getHandlerName() {
        return BotState.AUTH;
    }
}
