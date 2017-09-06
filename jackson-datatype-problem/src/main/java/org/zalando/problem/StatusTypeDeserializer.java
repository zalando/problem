package org.zalando.problem;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Map;

final class StatusTypeDeserializer extends JsonDeserializer<StatusType> {

    private final Map<Integer, StatusType> index;

    StatusTypeDeserializer(final Map<Integer, StatusType> index) {
        this.index = index;
    }

    @Override
    public StatusType deserialize(final JsonParser json, final DeserializationContext context) throws IOException {
        final int statusCode = json.getIntValue();
        @Nullable final StatusType status = index.get(statusCode);
        return status == null ? new UnknownStatus(statusCode) : status;
    }

}
