package org.zalando.problem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@JsonIgnoreProperties(ignoreUnknown = true)
interface ExceptionalWithoutStacktraceMixin extends ExceptionalMixin {

    @Override
    @JsonIgnore
    StackTraceElement[] getStackTrace();

}
