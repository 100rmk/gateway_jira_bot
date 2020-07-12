package ru.nesqui.gateway_jira_bot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.nesqui.gateway_jira_bot.GatewayJiraTelegramBot;
import ru.nesqui.gateway_jira_bot.handlers.TelegramFacade;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {
    private String botName;
    private String botToken;
    private String webHookPath;

    @Bean
    public GatewayJiraTelegramBot telegramBot(TelegramFacade telegramFacade) {
        GatewayJiraTelegramBot gatewayJiraTelegramBot = new GatewayJiraTelegramBot(telegramFacade);
        gatewayJiraTelegramBot.setBotName(botName);
        gatewayJiraTelegramBot.setBotToken(botToken);
        gatewayJiraTelegramBot.setWebHookPath(webHookPath);

        return gatewayJiraTelegramBot;
    }

}
