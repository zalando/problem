package org.zalando.problem.gson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.AllArgsConstructor;
import org.zalando.problem.Problem;
import org.zalando.problem.ProblemBuilder;
import org.zalando.problem.StatusType;
import org.zalando.problem.ThrowableProblem;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import static java.util.Arrays.stream;
import static lombok.AccessLevel.PRIVATE;

@AllArgsConstructor(access = PRIVATE)
final class DefaultProblemAdapter extends TypeAdapter<ThrowableProblem> {

    private final Gson gson;
    private final boolean stackTraces;
    private final TypeAdapter<String> stringAdapter;
    private final TypeAdapter<URI> uriAdapter;
    private final TypeAdapter<Map<String, Object>> parameters;
    private final TypeAdapter<StatusType> status;
    private final TypeAdapter<ThrowableProblem> cause;

    DefaultProblemAdapter(final Gson gson, final boolean stackTraces) {
        this(
                gson,
                stackTraces,
                gson.getAdapter(String.class),
                URITypeAdapter.TYPE,
                gson.getAdapter(new TypeToken<Map<String, Object>>() {
                    // nothing to do here
                }),
                gson.getAdapter(StatusType.class),
                gson.getAdapter(ThrowableProblem.class).nullSafe());
    }

    @Override
    public void write(final JsonWriter out, final ThrowableProblem problem) throws IOException {
        final JsonObject object = new JsonObject();

        object.add("type", uriAdapter.toJsonTree(problem.getType()));
        object.add("title", stringAdapter.toJsonTree(problem.getTitle()));
        object.add("status", status.toJsonTree(problem.getStatus()));
        object.add("detail", stringAdapter.toJsonTree(problem.getDetail()));
        object.add("instance", uriAdapter.toJsonTree(problem.getInstance()));
        object.add("cause", cause.toJsonTree(problem.getCause()));

        parameters.toJsonTree(problem.getParameters()).getAsJsonObject()
                .entrySet().forEach(entry ->
                object.add(entry.getKey(), entry.getValue()));

        if (stackTraces) {
            object.add("stacktrace", gson.getAdapter(String[].class)
                    .toJsonTree(stream(problem.getStackTrace())
                            .map(Object::toString)
                            .toArray(String[]::new)));
        }

        gson.getAdapter(JsonElement.class).write(out, object);
    }

    @Override
    public ThrowableProblem read(final JsonReader in) throws IOException {
        final ProblemBuilder builder = Problem.builder();

        in.beginObject();
        while (in.hasNext()) {
            final String name = in.nextName();
            switch (name) {
                case "type":
                    builder.withType(uriAdapter.read(in));
                    break;
                case "title":
                    builder.withTitle(stringAdapter.read(in));
                    break;
                case "status":
                    builder.withStatus(status.read(in));
                    break;
                case "detail":
                    builder.withDetail(stringAdapter.read(in));
                    break;
                case "instance":
                    builder.withInstance(uriAdapter.read(in));
                    break;
                case "cause":
                    builder.withCause(cause.read(in));
                    break;
                default:
                    builder.with(name, gson.fromJson(in, Object.class));
                    break;
            }
        }
        in.endObject();

        return builder.build();
    }
}
