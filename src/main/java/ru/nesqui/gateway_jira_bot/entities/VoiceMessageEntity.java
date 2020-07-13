package ru.nesqui.gateway_jira_bot.entities;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.Voice;

import java.io.Serializable;

@Setter
@Getter
public class VoiceMessageEntity extends Voice implements Serializable {
    private String result;
}
