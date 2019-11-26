package org.zalando.problem;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;

import java.net.URI;
import java.util.Arrays;

abstract class BaseAdapter<T> extends TypeAdapter<T> {

    protected static final TypeAdapter<URI> TYPE = TypeURIAdapter.INSTANCE;

    protected final Gson gson;
    private final boolean stackTraces;

    protected BaseAdapter(Gson gson, boolean stackTraces) {
        this.gson = gson;
        this.stackTraces = stackTraces;
    }

    protected final void serializeType(JsonObject out, URI type) {
        out.add("type", TYPE.toJsonTree(type));
    }

    protected final <T extends Throwable> void serializeStackTrace(JsonObject out, T value) {
        if (stackTraces) {
            String[] traces = Arrays.stream(value.getStackTrace())
                    .map(StackTraceElement::toString).toArray(String[]::new);
            TypeAdapter<String[]> adapter = gson.getAdapter(String[].class);
            out.add("stacktrace", adapter.toJsonTree(traces));
        }
    }

}
