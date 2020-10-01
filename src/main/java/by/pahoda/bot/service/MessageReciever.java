package by.pahoda.bot.service;

import by.pahoda.bot.Bot;
import by.pahoda.bot.command.Command;
import by.pahoda.bot.command.ParsedCommand;
import by.pahoda.bot.command.Parser;
import by.pahoda.bot.handler.AbstractHandler;
import by.pahoda.bot.handler.DefaultHandler;
import by.pahoda.bot.handler.SystemHandler;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.methods.send.SendMessage;

public class MessageReciever implements Runnable {
    private final int WAIT_FOR_NEW_MESSAGE_DELAY = 1000;
    private Bot bot;
    private Parser parser;

    public MessageReciever(Bot bot) {
        this.bot = bot;
        parser = new Parser(bot.getBotUsername());
    }

    @Override
    public void run() {
        while (true) {
            for (Object object = bot.receiveQueue.poll(); object != null; object = bot.receiveQueue.poll()) {
                System.out.println("New object for analyze in queue " + object.toString());
                analyze(object);
            }
            try {
                Thread.sleep(WAIT_FOR_NEW_MESSAGE_DELAY);
            } catch (InterruptedException e) {
                System.out.println("Catch interrupt. Exit" + e);
                return;
            }
        }
    }

    private void analyze(Object object) {
        if (object instanceof Update) {
            Update update = (Update) object;
            System.out.println("Update recieved: " + update.toString());
            analyzeForUpdateType(update);
        } else
            System.out.println("Cant operate type of object: " + object.toString());
    }

    private void analyzeForUpdateType(Update update) {
        Long chatId = update.getMessage().getChatId();
        String inputText = update.getMessage().getText();

        ParsedCommand parsedCommand = parser.getParsedCommand(inputText);
        AbstractHandler handlerForCommand = getHandlerForCommand(parsedCommand.getCommand());

        String operationResult = handlerForCommand.operate(chatId.toString(), parsedCommand, update);

        if (!"".equals(operationResult)) {
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(operationResult);
            bot.sendQueue.add(message);
        }
    }

    private AbstractHandler getHandlerForCommand(Command command) {
        if (command == null) {
            return new DefaultHandler(bot);
        }
        switch (command) {
            case START:
                return new SystemHandler(bot);
            case HELP:
                return new SystemHandler(bot);
            case ADD:
                return new SystemHandler(bot);
            case UPDATE:
                return new SystemHandler(bot);
            case DELETE:
                return new SystemHandler(bot);
            default:
                return new SystemHandler(bot);
        }
    }
}
