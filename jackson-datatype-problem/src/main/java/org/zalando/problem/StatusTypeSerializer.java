package org.zalando.problem;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

final class StatusTypeSerializer extends JsonSerializer<StatusType> {

    @Override
    public void serialize(final StatusType status, final JsonGenerator json, final SerializerProvider serializers) throws IOException {
        json.writeNumber(status.getStatusCode());
    }

}
