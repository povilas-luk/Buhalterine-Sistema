package webControllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import gsonSerializible.AllAccountingGsonSerializer;
import gsonSerializible.CategoryGsonSerializer;
import gsonSerializible.TransactionsGsonSerializer;
import hibernate.AccountingHibernateControl;
import hibernate.CategoryHibernateControl;
import hibernate.TransactionsHibernateControl;
import model.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Properties;

@Controller
public class WebTransactionsController {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("AccountingSystem");
    TransactionsHibernateControl transactionsHibernateControl = new TransactionsHibernateControl(emf);
    CategoryHibernateControl categoryHibernateControl = new CategoryHibernateControl(emf);

    @RequestMapping(value = "transactions/createIncome", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String createIncome(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String catName = data.getProperty("catName");
        String name = data.getProperty("name");
        String description = data.getProperty("description");
        String money = data.getProperty("money");

        Income income = new Income();
        Category category = categoryHibernateControl.findCategory(catName);

        income.setName(name);
        income.setDescription(description);
        income.setMoney(Integer.parseInt(money));

        try {
            transactionsHibernateControl.create(income);
            categoryHibernateControl.addTransactionToCategory(category, income);
        } catch (Exception e) {
            return "Failed to create income";
        }
        return "Income created";
    }

    @RequestMapping(value = "transactions/createExpense", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String createExpense(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String catName = data.getProperty("catName");
        String name = data.getProperty("name");
        String description = data.getProperty("description");
        String money = data.getProperty("money");

        Expense expense = new Expense();
        Category category = categoryHibernateControl.findCategory(catName);

        expense.setName(name);
        expense.setDescription(description);
        expense.setMoney(Integer.parseInt(money));

        try {
            transactionsHibernateControl.create(expense);
            categoryHibernateControl.addTransactionToCategory(category, expense);
        } catch (Exception e) {
            return "Failed to create expense";
        }
        return "Expense created";
    }

    @RequestMapping(value = "transactions/deleteIncome", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteIncome(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String catName = data.getProperty("catName");
        String name = data.getProperty("name");

        Income income = null;
        Category category = categoryHibernateControl.findCategory(catName);
        for (Transaction t : category.getTransactions()) {
            if (t.getName().equals(name)) {
                income = (Income)t;
            }
        }

        try {
            categoryHibernateControl.removeTransactionFromCategory(category, income);
            transactionsHibernateControl.destroy(income.getId());
        } catch (Exception e) {
            return "Failed to destroy income";
        }
        return "Income destroyed";
    }

    @RequestMapping(value = "transactions/deleteExpense", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteExpense(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String catName = data.getProperty("catName");
        String name = data.getProperty("name");

        Expense expense = null;
        Category category = categoryHibernateControl.findCategory(catName);
        for (Transaction t : category.getTransactions()) {
            if (t.getName().equals(name)) {
                expense = (Expense) t;
            }
        }

        try {
            categoryHibernateControl.removeTransactionFromCategory(category, expense);
            transactionsHibernateControl.destroy(expense.getId());
        } catch (Exception e) {
            return "Failed to destroy expense";
        }
        return "Expense destroyed";
    }

    @RequestMapping(value = "transactions/categoryTransactions", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getTransactionsByCategoryName(@RequestParam("name") String name) {

        if (name.equals("")) return "No Category name provided";

        Category category = categoryHibernateControl.findCategory(name);
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Transaction.class, new TransactionsGsonSerializer());
        Gson parser = gson.create();
        return parser.toJson(category.getTransactions());
    }

}
