package ru.smyshlyaev.HelperBot.HelperBotClient.Kafka;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.smyshlyaev.HelperBot.HelperBotClient.Services.PeopleService;

import java.util.Map;

@Service
public class KafkaConsumer {

    private static final Logger LOGGER = Logger.getLogger(KafkaConsumer.class);
    private final PeopleService peopleService;

    private String response;
    private String message;
    private long chatId;
    private long messageId;



    @Autowired
    public KafkaConsumer(PeopleService peopleService) {
        this.peopleService = peopleService;

    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public long getMessageId() {
        return messageId;
    }

    public long getChatId() {
        return chatId;
    }

    public void setId(long messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @KafkaListener(topics = {"telegramBotTopic"}, groupId = "myGroup")
    public void consume(Map<String, Object> eventMessage) {

        String fullName = (String) eventMessage.get("fullName");
        int messageId = (int) eventMessage.get("Id");
        String message = (String) eventMessage.get("message");
        String response = (String) eventMessage.get("response");

        LOGGER.info(String.format("fullName -> %s", fullName));
        LOGGER.info(String.format("message id -> %s", messageId));
        LOGGER.info(String.format("message -> %s", message));
        LOGGER.info(String.format("response -> %s", response));

        long chatId = peopleService.getPeopleByFullName(fullName).get().getTelegramId();

        setChatId(chatId);
        setResponse(response);
        setMessage(message);
        setId(messageId);

    }

}

