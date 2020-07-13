package ru.nesqui.gateway_jira_bot.handlers;

import com.google.common.collect.ImmutableCollection;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.jersey.internal.util.collection.ImmutableCollectors;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.nesqui.gateway_jira_bot.cache.UserDataCache;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class TelegramFacade {
    private MessageStateContext messageStateContext;
    private UserDataCache userDataCache;
    private BotState botState;
    private Set<Integer> verifiedUsersSetList = Stream.of(747641113, 86539097, 744938030)
            .collect(ImmutableCollectors.toImmutableSet());

    TelegramFacade(MessageStateContext messageStateContext, UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
        this.messageStateContext = messageStateContext;
    }

    //TODO: СДЕЛАТЬ ОБРАБОТКУ ГОЛОСОВОГО И ПОДУМАТЬ НАД ОБНУЛЕНИЕМ СОСТОЯНИЯ ПОСЛЕ ВЫЗОВА callbackQuery
    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage replyMessage = null;

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New CallBackQuery from User: {}, with data: {}",
                    update.getCallbackQuery().getFrom().getUserName(),
                    update.getCallbackQuery().getData());
            replyMessage = processCallbackQuery(callbackQuery);
        }

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User:{}, chatId: {}, with text: {}",
                    message.getFrom().getUserName(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }

        if (message != null && message.hasVoice()) {
            log.info("New voice message from User:{}, duration:{}, mimeType:{}",
                    message.getFrom().getUserName(), message.getVoice().getDuration(),
                    message.getVoice().getMimeType());
            replyMessage = handleVoiceMessage(message);
        }
        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) {
        String inputMessage = message.getText();
        int userId = message.getFrom().getId();
        SendMessage replyMessage;

        switch (inputMessage) {
            case "/start":
                botState = verifiedUsersSetList.contains(userId) ? BotState.AUTH : BotState.EMPTY;
                break;
            case "/menu":
                botState = verifiedUsersSetList.contains(userId) ? BotState.SHOW_TASK_MENU : BotState.EMPTY;
                break;
            default:
                botState = userDataCache.getUserCurrentBotState(userId);
        }

        // Устанавливаем текущему пользователю состояние
        userDataCache.setUserCurrentBotState(userId, botState);

        replyMessage = messageStateContext.processInputMessage(botState, message);

        return replyMessage;
    }

    private SendMessage processCallbackQuery(CallbackQuery buttonQuery) {
        int userId = buttonQuery.getFrom().getId();
        long chatId = buttonQuery.getMessage().getChatId();
        String buttonAnswerText = buttonQuery.getData();
        SendMessage replyMessage = null;


        switch (buttonAnswerText) {
            case "/create_task":
                botState = BotState.CREATE_TASK;
                replyMessage = new SendMessage()
                        .setChatId(chatId)
                        .setText("Запишите голосове сообщение или введите текст");
                break;
            case "/update_task":
                botState = BotState.UPDATE_TASK;
                replyMessage = new SendMessage().setChatId(chatId).setText("введите id задачи");
                break;
            case "/get_task":
                botState = BotState.GET_TASK;
                replyMessage = new SendMessage().setChatId(chatId)
                        .setText("введите id задачи");
                break;
            case "/delete_task":
                botState = BotState.DELETE_TASK;
                replyMessage = new SendMessage().setChatId(chatId)
                        .setText("введите id задачи");
                break;
            case "/get_all_tasks":
                botState = BotState.GET_ALL_TASKS;
                break;
            default:
                botState = userDataCache.getUserCurrentBotState(userId);
        }

        userDataCache.setUserCurrentBotState(userId, botState);

        return replyMessage;
    }

    private SendMessage handleVoiceMessage (Message message) {
        if (botState.equals(BotState.CREATE_TASK)) {
            return messageStateContext.processInputMessage(BotState.CREATE_TASK, message);
        }
        return new SendMessage()
                .setChatId(message.getChatId())
                .setText("Авторизируйтесь или выберите в меню \"Создать задачу\"");
    }
}
