package by.pahoda.bot.handler;

import by.pahoda.bot.Bot;
import by.pahoda.bot.command.ParsedCommand;
import org.telegram.telegrambots.api.objects.Update;

public class DefaultHandler extends AbstractHandler{
//    private static final Logger log = Logger.getLogger(DefaultHandler.class);

    public DefaultHandler(Bot bot) {
        super(bot);
    }

    @Override
    public String operate(String chatId, ParsedCommand parsedCommand, Update update) {
        return "";
    }
}
