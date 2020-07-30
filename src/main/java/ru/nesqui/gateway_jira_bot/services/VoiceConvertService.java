package ru.nesqui.gateway_jira_bot.services;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.nesqui.gateway_jira_bot.entities.VoiceMessageEntity;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
public class VoiceConvertService {
    @Value("${telegrambot.botToken}")
    private String botToken;
    @Value("${yandexSpeech.url}")
    private String yandexUrl;

    private RestTemplate restTemplate;
    private YandexService yandexService;
    private byte[] voiceByteArray;

    public VoiceConvertService(RestTemplateBuilder restTemplateBuilder, YandexService yandexService) {
        this.restTemplate = restTemplateBuilder.build();
        this.yandexService = yandexService;

    }

    public String getTextFromVoice(Message message) throws JSONException, IOException {
        String fileId = message.getVoice().getFileId();

        String filePathUrl = "https://api.telegram.org/bot" + botToken +
                "/getFile?file_id=" + fileId;
        JSONObject json = new JSONObject(IOUtils.toString(new URL(filePathUrl), StandardCharsets.UTF_8));

        String filePath = ((JSONObject) json.get("result")).get("file_path").toString();

        String downloadPath = "https://api.telegram.org/file/bot" + botToken +
                "/" + filePath;

        this.voiceByteArray = IOUtils.toByteArray(new URL(downloadPath));

        return getMessageFromByteArray();
    }

    private String getMessageFromByteArray() {
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", yandexService.getIAMToken());
        headers.add("Transfer-Encoding", "chunked");
        headers.add("Content-Type", "audio/ogg");

        HttpEntity<byte[]> entity = new HttpEntity<>(this.voiceByteArray, headers);

        ResponseEntity<VoiceMessageEntity> response = this.restTemplate.postForEntity(yandexUrl, entity, VoiceMessageEntity.class);
        return Objects.requireNonNull(response.getBody()).getResult();
    }
}
