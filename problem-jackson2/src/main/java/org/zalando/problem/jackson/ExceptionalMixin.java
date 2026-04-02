package org.zalando.problem.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.zalando.problem.ThrowableProblem;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonIgnoreProperties(ignoreUnknown = true)
interface ExceptionalMixin {

    @JsonIgnore
    String getMessage();

    @JsonIgnore
    String getLocalizedMessage();

    @JsonInclude(NON_NULL)
    ThrowableProblem getCause();

    // decision about inclusion is up to derived mixins
    @JsonProperty("stacktrace")
    @JsonSerialize(contentUsing = ToStringSerializer.class)
    StackTraceElement[] getStackTrace();

    @JsonIgnore
    Throwable[] getSuppressed();

}
