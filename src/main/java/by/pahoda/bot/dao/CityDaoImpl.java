package by.pahoda.bot.dao;

import by.pahoda.bot.City;
import by.pahoda.bot.HibernateConfig;
import org.hibernate.Session;
import java.sql.SQLException;
import java.util.List;

public class CityDaoImpl implements CityDao {
    @Override
    public void addCity(City city) throws SQLException {
        Session session = null;
        try{
            session = HibernateConfig.getSessionFactory().openSession();
            session.beginTransaction();
            session.saveOrUpdate(city);
            session.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if((session != null) && (session.isOpen())){
                session.close();
            }
        }
    }

    @Override
    public void deleteCity(City city) throws SQLException {
        Session session = null;
        try{
            session = HibernateConfig.getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(city);
            session.getTransaction().commit();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if((session != null) && (session.isOpen())){
                session.close();
            }
        }
    }

    @Override
    public City getCity(String name) throws SQLException {
        City result = null;
        Session session = null;
        try{
            session = HibernateConfig.getSessionFactory().openSession();
            result = session.load(City.class, name);
            System.out.println("1 " + result);
        } catch (Exception e){
            result = new City();
            result.setName(name);
            result.setDescription("");
        }

        return result;
    }
}
