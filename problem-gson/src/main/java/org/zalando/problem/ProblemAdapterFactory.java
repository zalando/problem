package org.zalando.problem;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.Streams;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import javax.annotation.CheckReturnValue;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Problem {@link TypeAdapterFactory}.
 */
public final class ProblemAdapterFactory implements TypeAdapterFactory {

    private final boolean stackTraces;
    private final Map<URI, Class<? extends Problem>> subtypes;
    private final StatusTypeAdapter statusAdapter;

    public ProblemAdapterFactory() {
        this(Status.class);
    }

    @SafeVarargs
    public <E extends Enum & StatusType> ProblemAdapterFactory(final Class<? extends E>... statusTypes) {
        this(false, new StatusTypeAdapter(buildIndex(statusTypes)), Collections.emptyMap());
    }

    private ProblemAdapterFactory(boolean stackTraces, StatusTypeAdapter statusAdapter,
                                  Map<URI, Class<? extends Problem>> subtypes) {
        this.stackTraces = stackTraces;
        this.statusAdapter = statusAdapter;
        this.subtypes = Collections.unmodifiableMap(subtypes);
    }

    @SafeVarargs
    private static <E extends Enum & StatusType> Map<Integer, StatusType> buildIndex(
            final Class<? extends E>... types) {
        final Map<Integer, StatusType> index = new HashMap<>();

        for (final Class<? extends E> type : types) {
            for (final E status : type.getEnumConstants()) {
                if (index.containsKey(status.getStatusCode())) {
                    throw new IllegalArgumentException("Duplicate status codes are not allowed");
                }
                index.put(status.getStatusCode(), status);
            }
        }

        return Collections.unmodifiableMap(index);
    }

    public ProblemAdapterFactory withStacktraces() {
        return withStacktraces(true);
    }

    public ProblemAdapterFactory withStacktraces(final boolean stackTraces) {
        return new ProblemAdapterFactory(stackTraces, this.statusAdapter, this.subtypes);
    }

    @CheckReturnValue
    public <T extends Problem> ProblemAdapterFactory registerSubtype(Class<T> clazz, URI type) {
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(type);
        if (subtypes.containsKey(type)) {
            throw new IllegalArgumentException("class & type must be unique");
        }
        HashMap<URI, Class<? extends Problem>> map = new HashMap<>(subtypes);
        map.put(type, clazz);
        return new ProblemAdapterFactory(stackTraces, statusAdapter, map);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<? super T> rawType = type.getRawType();

        if (StatusType.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) statusAdapter;
        }

        if (!Problem.class.isAssignableFrom(rawType)) {
            return null;
        }

        DefaultProblemAdapter defaultAdapter = new DefaultProblemAdapter(gson, stackTraces);
        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                TypeAdapter<T> adapter;
                if (value instanceof DefaultProblem) {
                    adapter = (TypeAdapter<T>) defaultAdapter;
                } else {
                    Class<T> valueClass = (Class<T>) value.getClass();
                    adapter = new CustomProblemAdapter<T>(gson, ProblemAdapterFactory.this, valueClass, stackTraces);
                }

                adapter.write(out, value);
            }

            @Override
            public T read(JsonReader in) {
                JsonElement element = Streams.parse(in);
                JsonObject jsonObject = element.getAsJsonObject();
                JsonElement typeElement = jsonObject.remove("type");
                typeElement = Optional.ofNullable(typeElement).orElse(JsonNull.INSTANCE);
                URI uri = TypeURIAdapter.INSTANCE.fromJsonTree(typeElement);
                jsonObject.add("type", TypeAdapters.URI.toJsonTree(uri));

                TypeAdapter<T> adapter = (TypeAdapter<T>) defaultAdapter;
                Class<? extends Problem> subType = subtypes.get(uri);
                if (Objects.nonNull(subType)) {
                    Class<T> typeClass = rawType.isAssignableFrom(subType) ? (Class<T>) subType : (Class<T>) rawType;
                    adapter = new CustomProblemAdapter<T>(gson, ProblemAdapterFactory.this, typeClass, stackTraces);
                }
                return adapter.fromJsonTree(element);
            }
        }.nullSafe();
    }

}
