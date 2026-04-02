package org.zalando.problem.jackson3;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import org.zalando.problem.StatusType;
import tools.jackson.databind.ValueSerializer;


final class StatusTypeSerializer extends ValueSerializer<StatusType> {

    @Override
    public void serialize(final StatusType status, final JsonGenerator json, final SerializationContext ctx) throws JacksonException {
        json.writeNumber(status.getStatusCode());
    }

}
