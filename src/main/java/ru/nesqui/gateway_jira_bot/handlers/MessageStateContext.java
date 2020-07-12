package ru.nesqui.gateway_jira_bot.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MessageStateContext {
    private Map<BotState, InputMessageHandler> messageHandler = new HashMap<>();

    public MessageStateContext(List<InputMessageHandler> messageHandler) {
        messageHandler.forEach(handler -> this.messageHandler.put(handler.getHandlerName(), handler));
    }

    public SendMessage processInputMessage(BotState currentState, Message message) {
        InputMessageHandler currentMessageHandler = findMessageHandler(currentState);
        return currentMessageHandler.handle(message);
    }


    private InputMessageHandler findMessageHandler(BotState currentState) {
        if (isAuth(currentState)) {
            return messageHandler.get(currentState);
        }

        return messageHandler.get(BotState.EMPTY);
    }

    private boolean isAuth(BotState currentState) {
        switch (currentState) {
            case GET_TASK:
            case CREATE_TASK:
            case DELETE_TASK:
            case UPDATE_TASK:
            case SHOW_TASK_MENU:
            case AUTH:
//            case CREATE_TEXT_TASK:
//            case CREATE_VOICE_TASK:
                return true;
            default:
                return false;
        }
    }
}
