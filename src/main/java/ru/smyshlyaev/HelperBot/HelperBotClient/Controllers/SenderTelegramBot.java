package ru.smyshlyaev.HelperBot.HelperBotClient.Controllers;

import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.smyshlyaev.HelperBot.HelperBotClient.Buttons.BotCommands;
import ru.smyshlyaev.HelperBot.HelperBotClient.Buttons.Buttons;
import ru.smyshlyaev.HelperBot.HelperBotClient.Configuration.BotConfig;
import ru.smyshlyaev.HelperBot.HelperBotClient.Kafka.KafkaConsumer;
import ru.smyshlyaev.HelperBot.HelperBotClient.Models.Message;
import ru.smyshlyaev.HelperBot.HelperBotClient.Services.MessageService;
import ru.smyshlyaev.HelperBot.HelperBotClient.Services.PeopleService;
import ru.smyshlyaev.HelperBot.HelperBotClient.Utils.MessageSenderException;
import ru.smyshlyaev.HelperBot.HelperBotClient.Utils.RegistrationException;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Component
@Controller
public class SenderTelegramBot extends TelegramLongPollingBot implements BotCommands {

    private static final Logger LOGGER=Logger.getLogger(SenderTelegramBot.class);
    private final PeopleService peopleService;
    private final MessageService messageService;
    private final BotConfig config;
    private final KafkaConsumer kafkaConsumer;

    @Autowired
    public SenderTelegramBot(PeopleService peopleService, MessageService messageService, BotConfig config,
                             KafkaConsumer kafkaConsumer) {


        this.peopleService = peopleService;
        this.messageService = messageService;
        this.config = config;
        this.kafkaConsumer = kafkaConsumer;
        try {
            this.execute(new SetMyCommands(LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e){
            LOGGER.error(e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public void onUpdateReceived(@NotNull Update update) { //метод onUpdateReceived(Update update) получает на вход объект update,
                                                           // из которого мы можем получить сообщение, текст и id чата, необходмые для отправки ответного сообщения.
        long chatId = 0;         // параметры пользователя
        long userId = 0;
        String fullName = null;
        String userName = null;
        String receivedMessage;

        //если получено сообщение текстом
        if(update.hasMessage()) {
            chatId = update.getMessage().getChatId();
            userId = update.getMessage().getFrom().getId();
            fullName = update.getMessage().getFrom().getFirstName();
            userName=update.getMessage().getFrom().getUserName();

            if (update.getMessage().hasText()) {
                receivedMessage = update.getMessage().getText();
                botAnswerUtils(receivedMessage, chatId, fullName, userId ,userName);

            }

            //если нажата одна из кнопок бота
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            userId = update.getCallbackQuery().getFrom().getId();
            fullName = update.getCallbackQuery().getFrom().getFirstName();
            userName=update.getCallbackQuery().getFrom().getUserName();
            receivedMessage = update.getCallbackQuery().getData();
            botAnswerUtils(receivedMessage, chatId, fullName, userId ,userName);

        }
    }

    private void botAnswerUtils(String receivedMessage, long chatId, String fullName,long userId,String userName) {

        switch (receivedMessage) {
            case "/start":
                startBot(chatId, userId, fullName, userName);
                checkStatus(kafkaConsumer);
                break;
            case "/help":
                sendHelpText(chatId, HELP_TEXT);
                getButtons(chatId);
                break;
            case "/registration":
                try {
                    peopleService.saveUser(userId, fullName, userName);
                    sendHelpText(chatId, REGISTRATION_TEXT);
                } catch (RegistrationException e) {
                    sendHelpText(chatId, e.getMessage());
                }
                getButtons(chatId);

                break;

            case "/my orders":
                List<Message> messages = peopleService.findMessageTelegramId(userId);
                if (messages.isEmpty()) {
                    sendHelpText(chatId, APP_TEXT);
                } else {
                    for (Message message : messages) {
                        String sendingMessage = "Номер заявки: " + message.getId() + "\n"
                                +"Сообщение: "  + message.getMessage()+ "\n"
                                + "Дата отправки: " + message.getCreatedAt() +"\n"
                                + "Статус " + message.getStatus() + "\n";
                        sendHelpText(chatId, sendingMessage);
                    }
                }
                getButtons(chatId);
                break;

            default:

                try {
                    if (peopleService.findOneUser(userId).isPresent()) {
                        messageService.saveMessage(receivedMessage, userId);
                        sendHelpText(chatId, SENDER_TEXT);
                    }
                } catch (MessageSenderException e) {
                    sendHelpText(chatId, e.getMessage());
                }

                break;
        }

    }
    private void startBot(long chatId,long userId, String fullName,String userName) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Привет, " + fullName + "! Добро пожаловать в телеграм бот. Данный телеграмм бот " +
                "предназначен для отправки различных запросов в какой-либо отдел(например IT отдел компании)." +
                "Для этого необходимо в чат бота написать возникшую проблему(составить заявку), кторая после попадет на сервер." +
                "Нажми на интересующую кнопку. Для помощи используй кнопку 'Help'");
        message.setReplyMarkup(Buttons.inlineMarkup());

        try {
            execute(message);
            LOGGER.info("Reply sent");
        } catch (TelegramApiException e){
            LOGGER.error(e.getMessage());
        }
    }
    private void getButtons(long chatId) {
        SendMessage message = new SendMessage();
        message.setText("Дальнейшие действия?");
        message.setChatId(chatId);
        message.setReplyMarkup(Buttons.inlineMarkup());

        try {
            execute(message);
            LOGGER.info("Reply sent");
        } catch (TelegramApiException e){
            LOGGER.error(e.getMessage());
        }
    }

    public void sendHelpText(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);

        try {
            execute(message);
            LOGGER.info("Reply sent");
        } catch (TelegramApiException e){
            LOGGER.error(e.getMessage());
        }
    }
    public void checkStatus(KafkaConsumer kafkaConsumer) {
        Timer timer = new Timer();

        timer.scheduleAtFixedRate(new TimerTask() {
            long tempValue=0;
            @Override
            public void run() {

                long getMessageIdFromKafka= kafkaConsumer.getMessageId();
                if(tempValue!=getMessageIdFromKafka) {
                    StringBuilder messageForTheUser= new StringBuilder("Ваша заявка: " +"'" + kafkaConsumer.getMessage() +"'" +"\n"
                            + "id заявки: " + kafkaConsumer.getMessageId()+ " ->" +"\n"
                            + kafkaConsumer.getResponse() +"\n"
                            + "Статус заявки изменен");
                    sendHelpText(kafkaConsumer.getChatId(), String.valueOf(messageForTheUser));
                    getButtons(kafkaConsumer.getChatId());
                }
                else{
                    LOGGER.info("Сообщений нет");
                }
                tempValue=kafkaConsumer.getMessageId();
                LOGGER.info("temp "+tempValue);
                LOGGER.info("kafka " + getMessageIdFromKafka);


            }
        }, 0, 5000); // 1000 миллисекунд = 1 секунда // каждые 5 секунд отправлятся запрос о наличии новых сообщений
    }


}


