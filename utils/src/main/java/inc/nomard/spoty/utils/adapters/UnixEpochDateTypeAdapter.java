package inc.nomard.spoty.utils.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.Date;

@Log4j2
public class UnixEpochDateTypeAdapter
        extends TypeAdapter<Date> {

    @Override
    public Date read(final JsonReader in)
            throws IOException {
        // this is where the conversion is performed
        return new Date(in.nextLong());
    }

    @Override
    public void write(final JsonWriter out, final Date value)
            throws IOException {
        // write back if necessary or throw UnsupportedOperationException
        out.value(value.getTime());
    }

}