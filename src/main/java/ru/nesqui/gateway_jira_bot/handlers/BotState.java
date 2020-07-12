package ru.nesqui.gateway_jira_bot.handlers;

public enum BotState {
    AUTH,
    SHOW_TASK_MENU,
    CREATE_TASK,
    GET_TASK,
    UPDATE_TASK,
    DELETE_TASK,
    GET_ALL_TASKS,
    EMPTY;
}
