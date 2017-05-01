/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restfulwebservices;


/**
 *
 * @author Merna
 */
public interface ConTAMServicesInterface {

    public void registerUser(String userStr);

    public String login(String primaryPhone, String password);

    public void updateUser(String userStr);

    public String addContact(String primaryPhone, String contactStr);

    public void updateContact(String contactStr);

    public void deleteContact(String contactStr);

    public String getAllContacts(String primaryPhone);
}
