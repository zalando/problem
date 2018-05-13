package org.zalando.problem;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
interface ExceptionalWithoutStacktraceMixin extends ExceptionalMixin {

    @Override
    @JsonIgnore
    StackTraceElement[] getStackTrace();

}
