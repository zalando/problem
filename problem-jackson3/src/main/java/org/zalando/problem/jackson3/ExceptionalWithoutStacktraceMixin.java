package org.zalando.problem.jackson3;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
interface ExceptionalWithoutStacktraceMixin extends ExceptionalMixin {

    @Override
    @JsonIgnore
    StackTraceElement[] getStackTrace();

}
