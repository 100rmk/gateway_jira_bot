package ru.nesqui.gateway_jira_bot.handlers.commands;

import okhttp3.*;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.nesqui.gateway_jira_bot.handlers.BotState;
import ru.nesqui.gateway_jira_bot.handlers.InputMessageHandler;
import ru.nesqui.gateway_jira_bot.services.YandexService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Component
public class CreateTaskHandler implements InputMessageHandler {
    @Value("${telegrambot.botToken}")
    private String botToken;

    private YandexService yandexService;

    @Value("${yandexSpeech.url}")
    private String yandexUrl;

    public CreateTaskHandler(YandexService yandexService) {
        this.yandexService = yandexService;
    }

    @Override
    public SendMessage handle(Message message) {
        SendMessage sendMessage;

        if (message.hasText()) {
            sendMessage = new SendMessage().setChatId(message.getChatId()).setText(message.getText());
        } else if (message.hasVoice()) {
            try {
                sendMessage = new SendMessage().setChatId(message.getChatId()).setText(speechToText(getVoiceUrl(message)));
            } catch (Exception e) {
                sendMessage = new SendMessage().setChatId(message.getChatId()).setText("Voice error");
                e.printStackTrace();
            }
        } else {
            sendMessage = new SendMessage().setChatId(message.getChatId()).setText("Empty message");
        }

        return sendMessage;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.CREATE_TASK;
    }

    private byte[] getVoiceUrl(Message message) throws JSONException, IOException {

        String fileId = message.getVoice().getFileId();

        String filePathUrl = "https://api.telegram.org/bot" + botToken +
                "/getFile?file_id=" + fileId;
        JSONObject json = new JSONObject(IOUtils.toString(new URL(filePathUrl), StandardCharsets.UTF_8));


        String filePath = ((JSONObject) json.get("result")).get("file_path").toString();

        String downloadPath = "https://api.telegram.org/file/bot" + botToken +
                "/" + filePath;

        return IOUtils.toByteArray(new URL(downloadPath));
    }

    public String speechToText(byte [] byteArray) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("audio/ogg");
        RequestBody body = RequestBody.create(mediaType, byteArray);
        Request request = new Request.Builder()
                .url(yandexUrl)
                .method("POST", body)
                .addHeader("Authorization", yandexService.getIAMToken())
                .addHeader("Transfer-Encoding", "chunked")
                .addHeader("Content-Type", "audio/ogg")
                .build();

        Response response = client.newCall(request).execute();
        JSONObject obj = new JSONObject(response.body().string());

        return obj.getString("result");
    }

}
