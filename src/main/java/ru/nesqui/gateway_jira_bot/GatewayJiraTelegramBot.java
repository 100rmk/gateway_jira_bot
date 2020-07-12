package ru.nesqui.gateway_jira_bot;

import lombok.Setter;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.nesqui.gateway_jira_bot.handlers.TelegramFacade;

@Setter
public class GatewayJiraTelegramBot extends TelegramWebhookBot {
    private String botName;
    private String botToken;
    private String webHookPath;

    private TelegramFacade telegramFacade;

    public GatewayJiraTelegramBot(TelegramFacade telegramFacade) {
        this.telegramFacade = telegramFacade;
    }

    @Override
    public BotApiMethod onWebhookUpdateReceived(Update update) {
        BotApiMethod replyMessage = telegramFacade.handleUpdate(update);

        return replyMessage;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }

}
