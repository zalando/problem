package org.zalando.problem;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Map;

import static com.google.gson.internal.bind.TypeAdapters.STRING;
import static com.google.gson.internal.bind.TypeAdapters.URI;

final class DefaultProblemAdapter extends BaseAdapter<DefaultProblem> {

    private static final TypeToken<Map<String, Object>> MAP_TYPE = new TypeToken<Map<String, Object>>() {};

    private final TypeAdapter<Map<String, Object>> mapAdapter;
    private final TypeAdapter<StatusType> statusAdapter;
    private final TypeAdapter<ThrowableProblem> throwableProblemAdapter;

    DefaultProblemAdapter(Gson gson, boolean stacktrace) {
        super(gson, stacktrace);
        this.mapAdapter = gson.getAdapter(MAP_TYPE);
        this.statusAdapter = gson.getAdapter(StatusType.class);
        this.throwableProblemAdapter = gson.getAdapter(ThrowableProblem.class).nullSafe();
    }

    @Override
    public void write(JsonWriter out, DefaultProblem problem) throws IOException {
        JsonObject jsonObject = new JsonObject();

        serializeType(jsonObject, problem.getType());
        jsonObject.add("title", STRING.toJsonTree(problem.getTitle()));
        jsonObject.add("status", statusAdapter.toJsonTree(problem.getStatus()));
        jsonObject.add("detail", STRING.toJsonTree(problem.getDetail()));
        jsonObject.add("instance", URI.toJsonTree(problem.getInstance()));
        jsonObject.add("cause", throwableProblemAdapter.toJsonTree(problem.getCause()));
        JsonObject paramsField = mapAdapter.toJsonTree(problem.getParameters()).getAsJsonObject();
        paramsField.entrySet().forEach(e -> jsonObject.add(e.getKey(), e.getValue()));
        serializeStackTrace(jsonObject, problem);

        gson.getAdapter(JsonElement.class).write(out, jsonObject);
    }

    @Override
    public DefaultProblem read(JsonReader in) throws IOException {
        ProblemBuilder builder = new ProblemBuilder();

        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            switch (name) {
                case "type":
                    builder.withType(TYPE.read(in));
                    break;
                case "title":
                    builder.withTitle(STRING.read(in));
                    break;
                case "status":
                    builder.withStatus(statusAdapter.read(in));
                    break;
                case "detail":
                    builder.withDetail(STRING.read(in));
                    break;
                case "instance":
                    builder.withInstance(URI.read(in));
                    break;
                case "cause":
                    builder.withCause(throwableProblemAdapter.read(in));
                    break;
                default:
                    builder.with(name, gson.fromJson(in, Object.class));
                    break;
            }
        }
        in.endObject();

        return (DefaultProblem) builder.build();
    }

}
