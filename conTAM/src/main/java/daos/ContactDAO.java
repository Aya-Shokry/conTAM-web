/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daos;

import java.util.List;
import java.util.Set;
import org.hibernate.Session;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import pojos.Contact;
import pojos.ContactPhones;
import pojos.User;

/**
 *
 * @author Merna
 */
public class ContactDAO {

    private static Session session;
    private static ContactDAO contactDAOInstance;

    private ContactDAO() {

        if (session == null) {

            Configuration configuration = new Configuration();
            configuration.configure("/hibernate.cfg.xml");

            session = configuration.buildSessionFactory(new StandardServiceRegistryBuilder().applySettings(
                    configuration.getProperties()).build()).openSession();
        }

    }

    public static synchronized ContactDAO getInstance() {
        if (contactDAOInstance == null) {
            contactDAOInstance = new ContactDAO();
        }
        return contactDAOInstance;
    }

    public Contact addContact(String primaryPhone, Contact contact) {

        session.beginTransaction();

        User user = (User) session.createQuery("from User user where user.phone =:primaryPhone")
                .setParameter("primaryPhone", primaryPhone)
                .uniqueResult();

        contact.setUser(user);
        session.persist(contact);

        Set<ContactPhones> contactPhonesSet = contact.getContactPhoneses();

        for (ContactPhones phone : contactPhonesSet) {
            phone.setContact(contact);
            session.persist(phone);
        }
        session.getTransaction().commit();

        contact.setContactPhoneses(null);
        return contact;
    }

    public void updateContact(Contact contact) {

        session.beginTransaction();
        session.update(contact);
        session.getTransaction().commit();

    }

    public void deleteContact(Contact contact) {

        session.beginTransaction();
        session.delete(contact);
        session.getTransaction().commit();

    }

    public List<Contact> getAllContacts(String primaryPhone) {

        return session.createQuery("from Contact contact where contact.user.phone =:primaryPhone")
                .setParameter("primaryPhone", primaryPhone).list();
    }

}
