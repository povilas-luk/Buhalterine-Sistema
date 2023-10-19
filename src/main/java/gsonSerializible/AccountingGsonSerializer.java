package gsonSerializible;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import model.Accounting;

import java.lang.reflect.Type;

public class AccountingGsonSerializer implements JsonSerializer<Accounting> {

    @Override
    public JsonElement serialize(Accounting accounting, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject accJson = new JsonObject();
        accJson.addProperty("accName", accounting.getAccName());
        return accJson;
    }
}
