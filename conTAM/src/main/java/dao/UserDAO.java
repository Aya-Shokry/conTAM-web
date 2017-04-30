package dao;

import java.util.List;
import java.util.Set;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import pojos.User;
import pojos.UserPhones;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Merna
 */
public class UserDAO {

    private static Session session = null;

    public UserDAO() {

        if (session == null) {
            Configuration configuration = new Configuration();
            configuration.configure("/hibernate.cfg.xml");

            session = configuration.buildSessionFactory(new StandardServiceRegistryBuilder().applySettings(
                    configuration.getProperties()).build()).openSession();
        }

    }

    public void registerUser(User user) {

        session.beginTransaction();
        session.persist(user);

        Set<UserPhones> userPhonesSet = user.getUserPhoneses();

        for (UserPhones phone : userPhonesSet) {
            phone.setUser(user);
            session.persist(phone);
        }

        session.getTransaction().commit();

    }

    public User login(String phone, String password) {

        Object user = session.createQuery("from User user where user.phone =:phone and user.password=:password")
                .setParameter("phone", phone)
                .setParameter("password", password).uniqueResult();

        if (user != null) {
            return (User) user;
        }

        return null;
    }

    public void updateUser(User user) {

        session.beginTransaction();
        session.update(user);
        session.getTransaction().commit();

    }

}
