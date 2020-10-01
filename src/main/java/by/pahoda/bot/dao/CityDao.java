package by.pahoda.bot.dao;

import by.pahoda.bot.City;
import java.sql.SQLException;

public interface CityDao {

    public void addCity(City city) throws SQLException;
    public void deleteCity(City city) throws SQLException;
    public City getCity(String name) throws SQLException;
}
