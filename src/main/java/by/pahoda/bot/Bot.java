package by.pahoda.bot;

import by.pahoda.bot.service.MessageReciever;
import by.pahoda.bot.service.MessageSender;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Bot extends TelegramLongPollingBot {
    public final Queue<Object> sendQueue = new ConcurrentLinkedQueue<>();
    public final Queue<Object> receiveQueue = new ConcurrentLinkedQueue<>();
    private static final int PRIORITY_FOR_SENDER = 1;
    private static final int PRIORITY_FOR_RECEIVER = 3;

    @Override
    public void onUpdateReceived(Update update) {
        receiveQueue.add(update);
    }

    @Override
    public String getBotUsername() {
        return "sshagd_bot";
    }

    @Override
    public String getBotToken() {
        return "1128395973:AAFKqbjpnfWMrcS3rP8PDmMQvvQUiR3-9io";
    }

    public static void main(String[] args) throws SQLException {
        ApiContextInitializer.init();
        Bot sshagd = new Bot();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        MessageReciever messageReciever = new MessageReciever(sshagd);
        MessageSender messageSender = new MessageSender(sshagd);

        Thread receiver = new Thread(messageReciever);
        receiver.setDaemon(true);
        receiver.setName("MsgReciever");
        receiver.setPriority(PRIORITY_FOR_RECEIVER);
        receiver.start();

        Thread sender = new Thread(messageSender);
        sender.setDaemon(true);
        sender.setName("MsgSender");
        sender.setPriority(PRIORITY_FOR_SENDER);
        sender.start();
        try {
            telegramBotsApi.registerBot(sshagd);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }
}
