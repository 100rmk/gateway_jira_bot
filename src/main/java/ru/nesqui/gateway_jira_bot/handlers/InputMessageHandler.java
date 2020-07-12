package ru.nesqui.gateway_jira_bot.handlers;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.nesqui.gateway_jira_bot.handlers.BotState;

public interface InputMessageHandler {
    SendMessage handle(Message message);

    BotState getHandlerName();
}