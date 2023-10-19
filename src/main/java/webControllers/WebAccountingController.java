package webControllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import gsonSerializible.AccountingGsonSerializer;
import gsonSerializible.AllAccountingGsonSerializer;
import hibernate.AccountingHibernateControl;
import model.Accounting;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.lang.reflect.Type;
import java.util.List;

@Controller
public class WebAccountingController {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("AccountingSystem");
    AccountingHibernateControl accountingHibernateControl = new AccountingHibernateControl(emf);

    @RequestMapping(value = "accounting/accList")
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllAccountings() {
        List<Accounting> accList = accountingHibernateControl.findAccountingEntities();

        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Accounting.class, new AccountingGsonSerializer());
        Gson parser = gson.create();
        parser.toJson(accList.get(0));

        Type libraryList = new TypeToken<List<Accounting>>() {
        }.getType();
        gson.registerTypeAdapter(libraryList, new AllAccountingGsonSerializer());
        parser = gson.create();

        return parser.toJson(accList);

    }

    @RequestMapping(value = "accounting/accInfo", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAccountingInfoByName(@RequestParam("name") String name) {

        if (name.equals("")) return "No library name provided";

        Accounting accounting = accountingHibernateControl.findAccounting(name);
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Accounting.class, new AccountingGsonSerializer());
        Gson parser = gson.create();
        return parser.toJson(accounting);
    }

}
