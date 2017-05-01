package daos;

import java.util.HashSet;
import java.util.Set;
import org.hibernate.Session;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import pojos.Contact;
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

    private static Session session;
    private static UserDAO userDAOInstance;

    private UserDAO() {

        if (session == null) {
            Configuration configuration = new Configuration();
            configuration.configure("/hibernate.cfg.xml");

            session = configuration.buildSessionFactory(new StandardServiceRegistryBuilder().applySettings(
                    configuration.getProperties()).build()).openSession();
        }

    }

    public static synchronized UserDAO getInstance() {
        if (userDAOInstance == null) {
            userDAOInstance = new UserDAO();
        }
        return userDAOInstance;
    }

    public void registerUser(User user) {

        session.beginTransaction();
        session.persist(user);
        session.getTransaction().commit();

    }

    public User login(String phone, String password) {

        Object user = session.createQuery("from User user where user.phone =:phone and user.password=:password")
                .setParameter("phone", phone)
                .setParameter("password", password).uniqueResult();

        if (user != null) {
            User userObj = (User) user;

            userObj.setContacts(null);
            for (Object userPhone : userObj.getUserPhoneses()) {
                ((UserPhones) userPhone).setUser(null);
            }

            return (User) user;
        }

        return null;
    }

    public void updateUser(User user) {

        user.setId((int) session.createQuery("select user.id from User user where user.phone =:phone")
                .setParameter("phone", user.getPhone())
                .uniqueResult());

        session.beginTransaction();
        session.update(user);
        
        for (Object phone : user.getUserPhoneses()) {
            ((UserPhones) phone).setUser(user);
            session.saveOrUpdate(phone);
        }

        session.getTransaction().commit();

    }

}
