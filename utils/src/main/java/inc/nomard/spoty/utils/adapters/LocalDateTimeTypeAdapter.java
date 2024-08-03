package inc.nomard.spoty.utils.adapters;

import com.google.gson.*;
import java.lang.reflect.*;
import java.time.*;
import java.time.format.*;
import lombok.extern.java.*;

@Log
public class LocalDateTimeTypeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return LocalDateTime.parse(json.getAsString(), FORMATTER);
    }

    @Override
    public JsonElement serialize(LocalDateTime time, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(time.format(FORMATTER));
    }
}