package ru.nesqui.gateway_jira_bot.cache;

import ru.nesqui.gateway_jira_bot.handlers.BotState;

public interface DataCache {
    void setUserCurrentBotState(int userId, BotState botState);

    BotState getUserCurrentBotState(int userId);
}
