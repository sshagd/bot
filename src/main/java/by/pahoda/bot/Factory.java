package by.pahoda.bot;

import by.pahoda.bot.dao.CityDao;
import by.pahoda.bot.dao.CityDaoImpl;

public class Factory {

    public static Factory instance = new Factory();
    public CityDao cityDao;

    private Factory(){}

    public static Factory getInstance(){
        return Factory.instance;
    }

    public CityDao getCityDao() {
        if(cityDao == null){
            cityDao = new CityDaoImpl();
        }
        return cityDao;
    }

}
