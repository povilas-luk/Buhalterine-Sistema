package webControllers;

import com.google.gson.Gson;
import hibernate.AccountingHibernateControl;
import hibernate.CategoryHibernateControl;
import hibernate.UserHibernateControl;
import model.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Properties;

@Controller
public class WebUserController {


    EntityManagerFactory emf = Persistence.createEntityManagerFactory("AccountingSystem");
    UserHibernateControl userHibernateControl = new UserHibernateControl(emf);
    CategoryHibernateControl categoryHibernateControl = new CategoryHibernateControl(emf);

    @RequestMapping(value = "user/login", method = RequestMethod.POST)
    //@RequestMapping(value = "user/login", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String login(@RequestBody String request) {

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String loginName = data.getProperty("login");
        String password = data.getProperty("psw");
        User user = userHibernateControl.findUserByLoginAndPassword(loginName, password);

        if (user == null) {
            return "Wrong credentials";
        }

        return user.getLoginName();
    }

    @RequestMapping(value = "user/updateConsumer", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateConsumer(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String loginName = data.getProperty("login");
        String password = data.getProperty("psw");
        String firstName = data.getProperty("name");
        String lastName = data.getProperty("surname");
        String email = data.getProperty("email");
        String phone = data.getProperty("phone");
        String address = data.getProperty("address");

        User user = userHibernateControl.findUser(loginName);

        if (user != null && user.getType().equals("consumer")) {
            user.setPassword(password);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPhoneNumber(phone);
            user.setAddress(address);
            try {
                userHibernateControl.edit(user);
            }catch (Exception e){
                return "Error while updating";
            }
        }
        return "Update successful";
    }

    @RequestMapping(value = "user/registerConsumer", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String registerConsumer(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String loginName = data.getProperty("login");
        String password = data.getProperty("psw");
        String firstName = data.getProperty("name");
        String lastName = data.getProperty("surname");
        String email = data.getProperty("email");
        String phone = data.getProperty("phone");
        String address = data.getProperty("address");

        System.out.println(request);

        System.out.println(loginName + ", " + password + ", " + firstName + ", " + lastName + ", " + email + ", " + password + ", " + address);

        Consumer user = new Consumer();

        user.setLoginName(loginName);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhoneNumber(phone);
        user.setAddress(address);

        try {
            userHibernateControl.create(user);
            //TODO accountingHibernateControl.addUserToAccounting(accounting, consumer);
        }catch (Exception e){
            return "Error while creating";
        }

        return "User created";
    }


    @RequestMapping(value = "/user/updateCompany", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateCompany(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String loginName = data.getProperty("login");
        String password = data.getProperty("psw");
        String firstName = data.getProperty("name");
        String lastName = data.getProperty("surname");
        String email = data.getProperty("email");
        String phone = data.getProperty("phone");
        String address = data.getProperty("address");
        String company = data.getProperty("company");

        User user = userHibernateControl.findUser(loginName);

        if (user != null && user.getType().equals("company")) {
            user.setPassword(password);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPhoneNumber(phone);
            user.setAddress(address);
            ((Company) user).setCompanyName(company);
            try {
                userHibernateControl.edit(user);
            }catch (Exception e){
                return "Error while updating";
            }
        }
        return "Update successful";
    }

    @RequestMapping(value = "user/registerCompany", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String registerCompany(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String loginName = data.getProperty("login");
        String password = data.getProperty("psw");
        String firstName = data.getProperty("name");
        String lastName = data.getProperty("surname");
        String email = data.getProperty("email");
        String phone = data.getProperty("phone");
        String address = data.getProperty("address");
        String company = data.getProperty("company");

        Company user = new Company();

        if (user.getType().equals("company")) {
            user.setLoginName(loginName);
            user.setPassword(password);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setPhoneNumber(phone);
            user.setAddress(address);
            user.setCompanyName(company);
            try {
                userHibernateControl.create(user);
            }catch (Exception e){
                return "Error while creating";
            }
        }
        return "User created";
    }

    @RequestMapping(value = "user/assignCategoryToUser", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String assignCategoryToUser(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String username = data.getProperty("username");
        String cat = data.getProperty("name");

        Category category = categoryHibernateControl.findCategory(cat);
        User user  = userHibernateControl.findUser(username);

        try {
            userHibernateControl.addCategoryToUser(user, category);
        } catch (Exception e) {
            return "Failed to add category";
        }
        return "Category added";
    }

    @RequestMapping(value = "user/removeCategoryFromUser", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String removeCategoryFromUser(@RequestBody String request) {

        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String username = data.getProperty("username");
        String cat = data.getProperty("name");

        Category category = categoryHibernateControl.findCategory(cat);
        User user  = userHibernateControl.findUser(username);

        try {
            userHibernateControl.removeCategoryFromUser(user, category);
        } catch (Exception e) {
            return "Failed to remove category";
        }
        return "Category removed";
    }

}
