package inc.nomard.spoty.utils.adapters;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateTypeAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return LocalDate.parse(json.getAsString(), FORMATTER);
    }

    @Override
    public JsonElement serialize(LocalDate time, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(time.format(FORMATTER));
    }
}