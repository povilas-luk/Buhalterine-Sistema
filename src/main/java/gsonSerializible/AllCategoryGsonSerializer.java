package gsonSerializible;

import com.google.gson.*;
import model.Accounting;
import model.Category;

import java.lang.reflect.Type;
import java.util.List;

public class AllCategoryGsonSerializer implements JsonSerializer<List<Category>> {
    @Override
    public JsonElement serialize(List<Category> categories, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonArray jsonArray = new JsonArray();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Category.class, new CategoryGsonSerializer());
        Gson parser = gsonBuilder.create();

        for (Category a : categories) {
            jsonArray.add(parser.toJson(a));
        }

        System.out.println(jsonArray);
        return jsonArray;
    }
}
