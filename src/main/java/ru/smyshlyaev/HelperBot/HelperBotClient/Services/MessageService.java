package ru.smyshlyaev.HelperBot.HelperBotClient.Services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smyshlyaev.HelperBot.HelperBotClient.Models.Message;
import ru.smyshlyaev.HelperBot.HelperBotClient.Models.People;
import ru.smyshlyaev.HelperBot.HelperBotClient.Repositories.MessageRepository;
import ru.smyshlyaev.HelperBot.HelperBotClient.Repositories.PeopleRepository;

import java.time.LocalDateTime;


@Service

public class MessageService {
    private final MessageRepository messageRepository;
    private final PeopleRepository peopleRepository;
    private static final Logger LOGGER=Logger.getLogger(People.class);


@Autowired
    public MessageService(MessageRepository messageRepository, PeopleRepository peopleRepository) {
    this.messageRepository = messageRepository;
    this.peopleRepository = peopleRepository;

}
     @Transactional
    public void saveMessage(String message, long userId) {
        Message newMessage= new Message();

            People senderMessage = peopleRepository.findPeopleByTelegramId(userId);

            newMessage.setSenderMessage(senderMessage);
            LOGGER.info(senderMessage.getUserName()+ " " + senderMessage.getTelegramId());
            newMessage.setSenderName(senderMessage.getFullName());
            newMessage.setMessage(message);
            newMessage.setCreatedAt(LocalDateTime.now());
            newMessage.setStatus("На рассмотрении");

            messageRepository.save(newMessage);

        }

}


