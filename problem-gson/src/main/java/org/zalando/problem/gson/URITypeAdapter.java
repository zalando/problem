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

    static final TypeAdapter<URI> TYPE = new URITypeAdapter(Problem.DEFAULT_TYPE);
    static final TypeAdapter<URI> INSTANCE = new URITypeAdapter(null);

    @Nullable
    private final URI defaultValue;

    private URITypeAdapter(@Nullable final URI defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public void write(final JsonWriter out, @Nullable final URI value) throws IOException {
        if (value == null || value.equals(defaultValue)) {
            out.nullValue();
        } else {
            out.value(value.toASCIIString());
        }
    }

    @Override
    @Nullable
    public URI read(final JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return defaultValue;
        }

        return URI.create(in.nextString());
    }

}
