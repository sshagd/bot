package by.pahoda.bot;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateConfig {

    private static SessionFactory sessionFactory;

    static {
        try{
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable e){
            throw new ExceptionInInitializerError(e);
        }
    }

    private HibernateConfig(){}

    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }
}
