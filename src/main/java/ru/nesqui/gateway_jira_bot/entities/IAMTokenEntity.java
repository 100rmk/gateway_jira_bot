package ru.nesqui.gateway_jira_bot.entities;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class IAMTokenEntity implements Serializable {
    private String iamToken;
    private String expiresAt;
}
