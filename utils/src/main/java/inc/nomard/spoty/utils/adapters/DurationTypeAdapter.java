package inc.nomard.spoty.utils.adapters;

import com.google.gson.*;
import com.google.gson.stream.*;
import java.io.*;
import java.time.*;
import lombok.extern.java.*;

@Log
public class DurationTypeAdapter extends TypeAdapter<Duration> {

    @Override
    public void write(JsonWriter out, Duration duration) throws IOException {
        if (duration == null) {
            out.nullValue();
        } else {
            out.value(duration.toString());
        }
    }

    @Override
    public Duration read(JsonReader in) throws IOException {
        if (in.peek() == null) {
            in.nextNull();
            return null;
        } else {
            return Duration.parse(in.nextString());
        }
    }
}
