package gsonSerializible;

import com.google.gson.*;
import model.Category;
import model.Transaction;

import java.lang.reflect.Type;
import java.util.List;

public class AllTransactionsGsonSerializer implements JsonSerializer<List<Transaction>> {

    @Override
    public JsonElement serialize(List<Transaction> transactions, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonArray = new JsonArray();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Transaction.class, new TransactionsGsonSerializer());
        Gson parser = gsonBuilder.create();

        for (Transaction t : transactions) {
            jsonArray.add(parser.toJson(t));
        }

        System.out.println(jsonArray);
        return jsonArray;
    }
}
