package ru.nesqui.gateway_jira_bot.handlers.commands;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.nesqui.gateway_jira_bot.handlers.BotState;
import ru.nesqui.gateway_jira_bot.handlers.InputMessageHandler;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class ShowMenuHandler implements InputMessageHandler {
    @Override
    public SendMessage handle(Message message) {
        List<List<InlineKeyboardButton>> commands = Arrays.asList(
                Collections.singletonList(
                        new InlineKeyboardButton().setText("Создать задачу").setCallbackData("/create_task")),
                Collections.singletonList(
                        new InlineKeyboardButton().setText("Изменить задачу").setCallbackData("/update_task")),
                Collections.singletonList(
                        new InlineKeyboardButton().setText("Получить задачу по id").setCallbackData("/get_task")),
                Collections.singletonList(
                        new InlineKeyboardButton().setText("Удалить задачу").setCallbackData("/delete_task")),
                Collections.singletonList(
                        new InlineKeyboardButton().setText("Получить список всех задач").setCallbackData("/get_all_tasks")));

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup()
                .setKeyboard(commands);

        return new SendMessage()
                .setChatId(message.getChatId())
                .setText("Menu")
                .setReplyMarkup(markupInline);
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_TASK_MENU;
    }
}
