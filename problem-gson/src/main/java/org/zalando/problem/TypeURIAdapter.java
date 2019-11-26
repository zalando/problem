package org.zalando.problem;

import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

final class TypeURIAdapter extends TypeAdapter<URI> {

    public static final TypeAdapter<URI> INSTANCE = new TypeURIAdapter();

    private TypeURIAdapter() {
        // Singleton.
    }

    @Override
    public void write(JsonWriter out, @Nullable URI value) throws IOException {
        out.value(value == null || value.equals(Problem.DEFAULT_TYPE) ? null : value.toASCIIString());
    }

    @Override
    public URI read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return Problem.DEFAULT_TYPE;
        }
        try {
            String nextString = in.nextString();
            return new URI(nextString);
        } catch (URISyntaxException e) {
            throw new JsonIOException(e);
        }
    }

}
