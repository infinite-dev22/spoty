package inc.nomard.spoty.core.viewModels.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;

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
