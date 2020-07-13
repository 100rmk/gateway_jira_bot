package ru.nesqui.gateway_jira_bot.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.nesqui.gateway_jira_bot.entities.IAMTokenEntity;
import ru.nesqui.gateway_jira_bot.entities.VoiceMessageEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class YandexService {
    private RestTemplate restTemplate;
    private String receivedToken;

    @Value("${yandexSpeech.IAMurl}")
    private String yandexUrl;

    private Map<String, String> valueMap = new HashMap<>();

    public YandexService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        valueMap.put("yandexPassportOauthToken", "AgAAAAAI1CYzAATuwYhASUaHQk6TjHmsSoSqGJM"); //TODO: разобраться почему переменная token = null
    }

    @Scheduled(fixedRate = 11 * 60 * 60 * 1000)
    public void updateIAMToken() {
        ResponseEntity<IAMTokenEntity> response =  this.restTemplate.postForEntity(yandexUrl, valueMap, IAMTokenEntity.class);
        this.receivedToken = Objects.requireNonNull(response.getBody()).getIamToken();
        log.info("Token has updated");
    }

    public String getIAMToken() {
        if (receivedToken == null) {
            this.updateIAMToken();
        }
        return "Bearer " + receivedToken;
    }
}
