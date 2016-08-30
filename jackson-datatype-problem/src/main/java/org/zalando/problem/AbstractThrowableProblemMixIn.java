package org.zalando.problem;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;
import javax.ws.rs.core.Response.StatusType;
import java.net.URI;

abstract class AbstractThrowableProblemMixIn {

    @JsonCreator
    AbstractThrowableProblemMixIn(
            @Nullable @JsonProperty("type") final URI type,
            @Nullable @JsonProperty("title") final String title,
            @Nullable @JsonProperty("status") final StatusType status,
            @Nullable @JsonProperty("detail") final String detail,
            @Nullable @JsonProperty("instance") final URI instance,
            @Nullable @JsonProperty("cause") final ThrowableProblem cause) {
        // this is just here to see whether "our" constructor matches the real one
        throw new AbstractThrowableProblem(type, title, status, detail, instance, cause) {

        };
    }

    @JsonAnySetter
    abstract void set(final String key, final Object value);

}
