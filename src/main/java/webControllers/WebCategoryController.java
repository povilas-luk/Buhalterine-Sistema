package webControllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gsonSerializible.AccountingGsonSerializer;
import gsonSerializible.CategoryGsonSerializer;
import hibernate.CategoryHibernateControl;
import hibernate.UserHibernateControl;
import model.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Controller
public class WebCategoryController {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("AccountingSystem");
    CategoryHibernateControl categoryHibernateControl = new CategoryHibernateControl(emf);
    UserHibernateControl userHibernateControl = new UserHibernateControl(emf);

    @RequestMapping(value = "category/createCategory", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String createCategory(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String name = data.getProperty("name");
        String description = data.getProperty("description");

        Category category = new Category();

        category.setName(name);
        category.setDescription(description);

        try {
            categoryHibernateControl.create(category);
        } catch (Exception e) {
            return "Failed to create category";
        }
        return "Category created";
    }

    @RequestMapping(value = "category/createSubCategory", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String createSubCategory(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String parentCat = data.getProperty("parentCat");
        String name = data.getProperty("name");
        String description = data.getProperty("description");

        Category category = new Category();
        Category parentCategory = categoryHibernateControl.findCategory(parentCat);

        category.setParentCategory(parentCategory);
        category.setName(name);
        category.setDescription(description);


        try {
            categoryHibernateControl.create(category);
            categoryHibernateControl.createSubCategory(category, parentCategory);
        } catch (Exception e) {
            return "Failed to create subcategory";
        }
        return "SubCategory created";
    }

    @RequestMapping(value = "category/userCategories", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getCategoriesByUserName(@RequestParam("name") String name) {

        if (name.equals("")) return "No User name provided";

        User user = userHibernateControl.findUser(name);
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Category.class, new CategoryGsonSerializer());
        Gson parser = gson.create();

        return parser.toJson(getUserCategories(user));

    }

    public List<Category> getUserCategories(User user) {
        List<Category> categories = new ArrayList<>();
        getCategories(user.getCategories(), categories);
        return categories;
    }

    public void getCategories(List<Category> UserCategories, List<Category> categories) {
        for (Category c : UserCategories) {
            categories.add(c);
            getCategories(c.getSubCategories(), categories);
        }
    }

    @RequestMapping(value = "category/deleteCategory", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteCategory(@RequestParam("name") String name) {

        if (name.equals("")) return "No library name provided";

        Category category = categoryHibernateControl.findCategory(name);
        try {
            categoryHibernateControl.destroy(category.getId());
            return "Deleted category";
        } catch (Exception e) {
            return "Failed to delete";
        }
    }

    @RequestMapping(value = "category/getCategoryBalance", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getCategoryBalance(@RequestParam("name") String name) {

        if (name.equals("")) return "No Category name provided";

        Category category = categoryHibernateControl.findCategory(name);
        try {
            return String.valueOf(getCategoryBalance(category));
        } catch (Exception e) {
            return "Failed to get balance";
        }
    }

    public int getCategoryBalance(Category category) {
        int balance = 0;
        for (Transaction t : category.getTransactions()) {
            if (t.getType().equals("income")) {
                balance += t.getMoney();
            } else if (t.getType().equals("expense")) {
                balance -= t.getMoney();
            }
        }
        return balance;
    }

    @RequestMapping(value = "category/getUserBalance", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getUserBalance(@RequestParam("name") String name) {

        if (name.equals("")) return "No Category name provided";

        User user = userHibernateControl.findUser(name);
        try {
            return String.valueOf(getUserBalance(user));
        } catch (Exception e) {
            return "Failed to get balance";
        }
    }

    public int getUserBalance(User user) {
        int balance = 0;
        for (Category c : getUserCategories(user)) {
            balance += getCategoryBalance(c);
        }
        return balance;
    }
}
