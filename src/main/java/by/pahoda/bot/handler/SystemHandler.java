package by.pahoda.bot.handler;

import by.pahoda.bot.Bot;
import by.pahoda.bot.City;
import by.pahoda.bot.Factory;
import by.pahoda.bot.command.Command;
import by.pahoda.bot.command.ParsedCommand;
import by.pahoda.bot.dao.CityDao;
import by.pahoda.bot.dao.CityDaoImpl;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import java.sql.SQLException;

public class SystemHandler extends AbstractHandler {
    private final String END_LINE = "\n";

    public SystemHandler(Bot bot) {
        super(bot);
    }

    Factory factory = Factory.getInstance();
    CityDao cityDao = factory.getCityDao();

    @Override
    public String operate(String chatId, ParsedCommand parsedCommand, Update update) {
        Command command = parsedCommand.getCommand();
        String txt = update.getMessage().getText();

        switch (command) {
            case START:
                bot.sendQueue.add(getMessageStart(chatId));
                break;
            case HELP:
                bot.sendQueue.add(getMessageHelp(chatId));
            case ADD:
                bot.sendQueue.add(getMessageAdd(chatId, parsedCommand.getText()));
                break;
            case UPDATE:
                bot.sendQueue.add(getMessageUpdate(chatId, parsedCommand.getText()));
                break;
            case DELETE:
                bot.sendQueue.add(getMessageDelete(chatId, parsedCommand.getText()));
                break;
            default:
                bot.sendQueue.add(getMessageGet(chatId, parsedCommand.getText()));
                break;
        }
        return "";
    }

    private SendMessage getMessageGet(String chatID, String name){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.enableMarkdown(true);
        City city;
        String message = "";

        try {
            city = dao.getCity(name);
            if(!city.getDescription().equals("")){
                message = city.getDescription();
            } else {
                message = "Такого города нет в базе, но его можно добавить с помощью команды /*add*";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sendMessage.setText(message);
        return sendMessage;
    }

    private SendMessage getMessageHelp(String chatID) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.enableMarkdown(true);

        StringBuilder text = new StringBuilder();
        text.append("*Справка*").append(END_LINE).append(END_LINE);
        text.append("[/start](/start) - приветствующее сообщение").append(END_LINE);
        text.append("[/help](/help) - справка").append(END_LINE);
        text.append("*город* - информация о городе (case insensitive)").append(END_LINE);
        text.append("/*add* _город=описание_  - добавление города").append(END_LINE);
        text.append("/*update* _город=описание_  - изменение информации о городе").append(END_LINE);
        text.append("/*delete* _город_  - удаление города").append(END_LINE);

        sendMessage.setText(text.toString());
        return sendMessage;
    }

    private CityDaoImpl dao = new CityDaoImpl();
    private SendMessage getMessageStart(String chatID) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.enableMarkdown(true);
        StringBuilder text = new StringBuilder();
        text.append("Привет, я туристический бот *").append(bot.getBotUsername()).append("*").append(END_LINE);
        text.append("Узнать о моих способностях можно по команде [/help](/help)");
        sendMessage.setText(text.toString());
        return sendMessage;
    }
    private SendMessage getMessageAdd(String chatID, String text){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.enableMarkdown(true);
        String[] newCity = text.split("=", 2);
        City city = new City();
        city.setName(newCity[0]);
        city.setDescription(newCity[1]);

        try {
            dao.addCity(city);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sendMessage.setText("Город " + city.getName() + " добавлен.");
        return sendMessage;
    }
    private SendMessage getMessageUpdate(String chatID, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.enableMarkdown(true);
        String[] newCity = text.split("=", 2);
        City city = new City();
        city.setName(newCity[0]);
        city.setDescription(newCity[1]);
        try {
            dao.addCity(city);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sendMessage.setText("Город " + city.getName() + " обновлён.");
        return sendMessage;
    }
    private SendMessage getMessageDelete(String chatID, String name) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatID);
        sendMessage.enableMarkdown(true);
        City city = new City();
        city.setName(name);
        try {
            dao.deleteCity(city);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        sendMessage.setText("Город " + name + " удалён.");
        return sendMessage;
    }
}
