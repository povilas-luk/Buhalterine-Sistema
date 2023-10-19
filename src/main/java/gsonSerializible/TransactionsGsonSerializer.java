package gsonSerializible;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.Transaction;

import java.lang.reflect.Type;

public class TransactionsGsonSerializer implements JsonSerializer<Transaction> {

    @Override
    public JsonElement serialize(Transaction transaction, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject tranJson = new JsonObject();
        tranJson.addProperty("Type: ", transaction.getType());
        tranJson.addProperty("Name: ", transaction.getName());
        tranJson.addProperty("Description: ", transaction.getDescription());
        tranJson.addProperty("Money: ", transaction.getMoney());
        return tranJson;
    }
}
