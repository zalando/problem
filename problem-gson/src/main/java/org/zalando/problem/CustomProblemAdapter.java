package org.zalando.problem;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

final class CustomProblemAdapter<T> extends BaseAdapter<T> {

    private TypeAdapter<T> delegate;

    CustomProblemAdapter(Gson gson, ProblemAdapterFactory parent, Class<T> type, boolean stackTraces) {
        super(gson, stackTraces);
        this.delegate = gson.getDelegateAdapter(parent, TypeToken.get(type));
    }

    @Override
    public void write(JsonWriter out, T value) throws IOException {
        JsonElement element = delegate.toJsonTree(value);
        JsonObject jsonObject = element.getAsJsonObject();

        // Handle `type` field.
        URI problemType = TYPE.fromJsonTree(jsonObject.remove("type"));
        serializeType(jsonObject, problemType);

        if (value instanceof AbstractThrowableProblem) {
            // Flatten `parameters` field if AbstractThrowableProblem.
            Optional.ofNullable(jsonObject.remove("parameters"))
                    .map(JsonElement::getAsJsonObject)
                    .ifPresent(params -> params.entrySet().forEach(e -> jsonObject.add(e.getKey(), e.getValue())));
        }

        if (value instanceof Throwable) {
            // Get rid of unwanted fields.
            jsonObject.remove("detailMessage");
            jsonObject.remove("suppressedExceptions");
            jsonObject.remove("stackTrace");
            serializeStackTrace(jsonObject, (Throwable) value);
        }

        Streams.write(element, out);
    }

    @Override
    public T read(JsonReader in) throws IOException {
        return delegate.read(in);
    }
}
