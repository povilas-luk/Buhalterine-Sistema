package gsonSerializible;

import com.google.gson.*;
import model.Accounting;
import model.Category;

import java.lang.reflect.Type;

public class CategoryGsonSerializer implements JsonSerializer<Category> {

    @Override
    public JsonElement serialize(Category category, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject catJson = new JsonObject();
        catJson.addProperty("id", category.getId());
        catJson.addProperty("name", category.getName());
        catJson.addProperty("description", category.getDescription());
        if (category.getParentCategory() != null) {
            catJson.addProperty("parentCategoryId", category.getParentCategory().getId());
        } else {
            catJson.addProperty("parentCategoryId", -1);
        }
        Gson gs = new Gson();
        catJson.addProperty("subCategories", category.getSubCategoryIdAsString());

        return catJson;
    }
}
