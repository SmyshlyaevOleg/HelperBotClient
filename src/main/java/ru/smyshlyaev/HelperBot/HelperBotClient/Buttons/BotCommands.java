package ru.smyshlyaev.HelperBot.HelperBotClient.Buttons;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public interface BotCommands {
    List<BotCommand> LIST_OF_COMMANDS = List.of(
            new BotCommand("/start", "запуск бота"),
            new BotCommand("/help", "информация о боте"),
            new BotCommand("/registration", "регистрация новых пользователей"),
            new BotCommand("/my orders", "Просмотр моих заявок")
    );

    String HELP_TEXT = "Данный бот создан для отправки заявок возникших проблем в IT отдел. Вам небходимо отправить сообщение о взникшей проблеме в данный телеграмм бот \n" +
            "Инфрмация по кнопкам:\n\n" +
            "/start - кнопка старта бота\n" +
            "/help - кнопка вызова помощи\n" +
            "/registration - кнопка для регистрации в системе(извините без регистрации не обойтись)\n " +
            "/my_orders - кнопка вызова просмотра всех отправленных заявок\n";
    String REGISTRATION_TEXT="Регистрация прошла успешно";
    String SENDER_TEXT="Сообщение успешно сохранено в базе данных!";
    String APP_TEXT="Пока заявок нет!";

}
