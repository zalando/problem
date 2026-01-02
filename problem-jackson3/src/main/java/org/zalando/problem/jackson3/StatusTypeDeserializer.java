package org.zalando.problem.jackson3;

import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.zalando.problem.StatusType;
import tools.jackson.databind.ValueDeserializer;

import java.util.Map;

final class StatusTypeDeserializer extends ValueDeserializer<StatusType> {

    private final Map<Integer, StatusType> index;

    StatusTypeDeserializer(final Map<Integer, StatusType> index) {
        this.index = index;
    }

    @Override
    public StatusType deserialize(final JsonParser json, final DeserializationContext context) {
        final int statusCode = json.getIntValue();
        @Nullable final StatusType status = index.get(statusCode);
        return status == null ? new UnknownStatus(statusCode) : status;
    }

}
