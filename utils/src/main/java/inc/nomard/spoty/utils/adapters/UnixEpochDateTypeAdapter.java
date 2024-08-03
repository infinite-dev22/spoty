package inc.nomard.spoty.utils.adapters;

import com.google.gson.*;
import com.google.gson.stream.*;
import java.io.*;
import java.util.*;
import lombok.extern.java.*;

@Log
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