package org.zalando.problem;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

final class StatusTypeAdapter extends TypeAdapter<StatusType> {

    private Map<Integer, StatusType> index;

    StatusTypeAdapter(Map<Integer, StatusType> statuses) {
        this.index = statuses;
    }

    @Override
    public void write(JsonWriter out, @Nullable StatusType status) throws IOException {
        if (Objects.isNull(status)) {
            out.nullValue();
            return;
        }
        out.value(status.getStatusCode());
    }

    @Override
    public StatusType read(JsonReader in) throws IOException {
        JsonToken peek = in.peek();
        if (JsonToken.NULL == peek) {
            in.nextNull();
            return null;
        }

        final int statusCode = in.nextInt();
        @Nullable final StatusType status = index.get(statusCode);
        return status == null ? new UnknownStatus(statusCode) : status;
    }

}
