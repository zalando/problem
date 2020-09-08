package org.zalando.problem.jackson;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.StatusType;
import org.zalando.problem.ThrowableProblem;

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
