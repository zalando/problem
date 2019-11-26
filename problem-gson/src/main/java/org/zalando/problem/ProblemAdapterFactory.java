package org.zalando.problem;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.AllArgsConstructor;
import org.apiguardian.api.API;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;
import static org.apiguardian.api.API.Status.EXPERIMENTAL;
import static org.zalando.problem.URITypeAdapter.TYPE;

/**
 * Problem {@link TypeAdapterFactory}.
 */
@API(status = EXPERIMENTAL)
public final class ProblemAdapterFactory implements TypeAdapterFactory {

    private final boolean stackTraces;
    private final Map<URI, TypeToken<? extends Problem>> subtypes;
    private final StatusTypeAdapter statusAdapter;

    public ProblemAdapterFactory() {
        this(Status.class);
    }

    @SafeVarargs
    public <E extends Enum & StatusType> ProblemAdapterFactory(
            final Class<? extends E>... statusTypes) {
        this(false,
                new StatusTypeAdapter(buildIndex(statusTypes)),
                Collections.emptyMap());
    }

    private ProblemAdapterFactory(
            final boolean stackTraces,
            final StatusTypeAdapter statusAdapter,
            final Map<URI, TypeToken<? extends Problem>> subtypes) {
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
                    throw new IllegalArgumentException(
                            "Duplicate status codes are not allowed");
                }
                index.put(status.getStatusCode(), status);
            }
        }

        return Collections.unmodifiableMap(index);
    }

    public ProblemAdapterFactory withStackTraces() {
        return withStackTraces(true);
    }

    public ProblemAdapterFactory withStackTraces(final boolean stackTraces) {
        return new ProblemAdapterFactory(stackTraces, statusAdapter, subtypes);
    }

    @CheckReturnValue
    public ProblemAdapterFactory registerSubtype(
            final URI uri, final Class<? extends Problem> type) {

        return registerSubType(uri, TypeToken.get(type));
    }

    @CheckReturnValue
    public ProblemAdapterFactory registerSubType(
            final URI uri, final TypeToken<? extends Problem> type) {

        requireNonNull(type, "Type");
        requireNonNull(uri, "URI");

        if (subtypes.containsKey(uri)) {
            throw new IllegalArgumentException("class & type must be unique");
        }

        final Map<URI, TypeToken<? extends Problem>> map = new HashMap<>(subtypes);
        map.put(uri, type);
        return new ProblemAdapterFactory(stackTraces, statusAdapter, map);

    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
        final Class<? super T> rawType = type.getRawType();

        if (StatusType.class.isAssignableFrom(rawType)) {
            return (TypeAdapter<T>) statusAdapter;
        }

        if (!Problem.class.isAssignableFrom(rawType)) {
            return null;
        }

        return new ProblemTypeAdapter<T>(gson, type).nullSafe();
    }

    @AllArgsConstructor(access = PRIVATE)
    private final class ProblemTypeAdapter<T> extends TypeAdapter<T> {

        private final Gson gson;
        private final TypeToken<T> type;
        private final TypeAdapter<ThrowableProblem> defaultAdapter;

        ProblemTypeAdapter(final Gson gson, final TypeToken<T> type) {
            this(gson, type, new DefaultProblemAdapter(gson, stackTraces));
        }

        @Override
        public void write(final JsonWriter out, final T value) throws IOException {
            final TypeAdapter<T> adapter = selectAdapter(value);
            adapter.write(out, value);
        }

        @SuppressWarnings("unchecked")
        private TypeAdapter<T> selectAdapter(final T value) {
            if (value instanceof DefaultProblem) {
                return (TypeAdapter<T>) defaultAdapter;
            } else {
                final Class<T> valueType = (Class<T>) value.getClass();
                return createCustomAdapter(gson, TypeToken.get(valueType));
            }
        }

        @Override
        public T read(final JsonReader in) {
            final JsonElement element = Streams.parse(in);
            final JsonObject problem = element.getAsJsonObject();
            return selectAdapter(problem).fromJsonTree(element);
        }

        @SuppressWarnings("unchecked")
        private TypeAdapter<T> selectAdapter(final JsonObject problem) {
            @Nullable final TypeToken<? extends Problem> subType =
                    Optional.ofNullable(problem.get("type"))
                            .map(TYPE::fromJsonTree)
                            .map(subtypes::get)
                            .orElse(null);

            if (subType == null) {
                return (TypeAdapter<T>) defaultAdapter;
            }

            final TypeToken<T> typeClass =
                    (type.getRawType().isAssignableFrom(subType.getRawType()) ?
                            (TypeToken<T>) subType :
                            type);

            return createCustomAdapter(gson, typeClass);
        }

        private TypeAdapter<T> createCustomAdapter(
                final Gson gson, final TypeToken<T> type) {

            return new CustomProblemAdapter<>(
                    gson,
                    gson.getDelegateAdapter(
                            ProblemAdapterFactory.this,
                            type),
                    stackTraces);
        }

    }

}
