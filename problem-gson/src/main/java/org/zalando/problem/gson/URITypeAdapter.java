package org.zalando.problem.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.zalando.problem.Problem;

import java.io.IOException;
import java.net.URI;

final class URITypeAdapter extends TypeAdapter<URI> {

    static final TypeAdapter<URI> TYPE = new URITypeAdapter();

    private URITypeAdapter() {
        // Singleton.
    }

    @Override
    public void write(final JsonWriter out, @Nullable final URI value) throws IOException {
        if (value == null || value.equals(Problem.DEFAULT_TYPE)) {
            out.nullValue();
        } else {
            out.value(value.toASCIIString());
        }
    }

    @Override
    public URI read(final JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return Problem.DEFAULT_TYPE;
        }

        return URI.create(in.nextString());
    }

}
