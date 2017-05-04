package restfulwebservices;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import daos.ContactDAO;
import daos.UserDAO;
import java.util.List;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import pojos.Contact;
import pojos.User;
import pojos.UserPhones;

/**
 *
 * @author Merna
 */
@Path("/service")
public class ConTAMServices implements ConTAMServicesInterface {

    @GET
    @Path("/registerUser")
    @Override
    public void registerUser(@QueryParam(value = "user") String userStr) {

        UserDAO.getInstance().registerUser(new Gson().fromJson(userStr, User.class));
    }

    @GET
    @Path("/login")
    @Produces("application/json")
    @Override
    public String login(@QueryParam(value = "primaryPhone") String primaryPhone,
            @QueryParam(value = "password") String password) {

        return new Gson().toJson(UserDAO.getInstance().login(primaryPhone, password), User.class);
    }

    @GET
    @Path("/updateUser")
    @Override
    public void updateUser(@QueryParam(value = "user") String userStr) {

        Gson gson = new Gson();
        User user = gson.fromJson(userStr, User.class);
        Set<UserPhones> phones = gson.fromJson(user.getUserPhoneses().toString(), new TypeToken<Set<UserPhones>>() {
        }.getType());

        user.setUserPhoneses(phones);
        UserDAO.getInstance().updateUser(user);
    }

    @GET
    @Path("/addContact")
    @Produces("application/json")
    @Override
    public String addContact(@QueryParam(value = "primaryPhone") String primaryPhone,
            @QueryParam(value = "contact") String contactStr) {

        Gson gson = new Gson();
        return gson.toJson(ContactDAO.getInstance().addContact(primaryPhone, gson.fromJson(contactStr, Contact.class)), Contact.class);

    }

    @GET
    @Path("/updateContact")
    @Produces("application/json")
    @Override
    public void updateContact(@QueryParam(value = "contact") String contactStr) {

        ContactDAO.getInstance().updateContact(new Gson().fromJson(contactStr, Contact.class));
    }

    @GET
    @Path("/deleteContact")
    @Override
    public void deleteContact(@QueryParam(value = "contact") String contactStr) {

        ContactDAO.getInstance().deleteContact(new Gson().fromJson(contactStr, Contact.class));
    }

    @GET
    @Path("/getAllContacts")
    @Produces("application/json")
    @Override
    public String getAllContacts(@QueryParam(value = "primaryPhone") String primaryPhone) {

        return new Gson().toJson(ContactDAO.getInstance().getAllContacts(primaryPhone), new TypeToken<List<Contact>>() {
        }.getType());
    }

}
