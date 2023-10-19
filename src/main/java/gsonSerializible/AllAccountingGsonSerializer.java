package gsonSerializible;

import com.google.gson.*;
import model.Accounting;

import java.lang.reflect.Type;
import java.util.List;

public class AllAccountingGsonSerializer implements JsonSerializer<List<Accounting>> {
    @Override
    public JsonElement serialize(List<Accounting> accList, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonArray = new JsonArray();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Accounting.class, new AccountingGsonSerializer());
        Gson parser = gsonBuilder.create();

        for (Accounting a : accList) {
            jsonArray.add(parser.toJson(a));
        }

        System.out.println(jsonArray);
        return jsonArray;
    }
}
