package ru.nesqui.gateway_jira_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@EnableScheduling
public class GatewayJiraBotApplication {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(GatewayJiraBotApplication.class, args);
    }
}
