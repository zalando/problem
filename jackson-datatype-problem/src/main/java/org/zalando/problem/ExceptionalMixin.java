package org.zalando.problem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonIgnoreProperties(ignoreUnknown = true)
interface ExceptionalMixin {

    @JsonIgnore
    String getMessage();

    @JsonIgnore
    String getLocalizedMessage();

    @JsonInclude(NON_NULL)
    ThrowableProblem getCause();

    @JsonIgnore
    StackTraceElement[] getStackTrace();

    @JsonIgnore
    Throwable[] getSuppressed();

}
