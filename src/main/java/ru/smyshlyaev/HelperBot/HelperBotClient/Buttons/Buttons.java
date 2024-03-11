package ru.smyshlyaev.HelperBot.HelperBotClient.Buttons;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class Buttons {
    private static final InlineKeyboardButton START_BUTTON = new InlineKeyboardButton("Start");
    private static final InlineKeyboardButton HELP_BUTTON = new InlineKeyboardButton("Help");
    private static final InlineKeyboardButton REGISTRATION_BUTTON = new InlineKeyboardButton("Registration");
    private static final InlineKeyboardButton STATEMENTS_BUTTON = new InlineKeyboardButton("My orders");

    public static InlineKeyboardMarkup inlineMarkup() {
        START_BUTTON.setCallbackData("/start");
        HELP_BUTTON.setCallbackData("/help");
        REGISTRATION_BUTTON.setCallbackData("/registration");
        STATEMENTS_BUTTON.setCallbackData("/my orders");


        List<InlineKeyboardButton> rowInline = List.of(START_BUTTON, HELP_BUTTON,REGISTRATION_BUTTON,STATEMENTS_BUTTON);
        List<List<InlineKeyboardButton>> rowsInLine = List.of(rowInline);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        markupInline.setKeyboard(rowsInLine);

        return markupInline;
    }
}
