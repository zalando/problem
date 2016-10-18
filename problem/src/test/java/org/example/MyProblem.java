package org.example;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.ThrowableProblem;

import javax.annotation.Nullable;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Map;

@SuppressWarnings("unused") // since we're testing access levels we're fine if this compiles
public final class MyProblem extends AbstractThrowableProblem {

    MyProblem() {

    }

    MyProblem(@Nullable final URI type) {
        super(type);
    }

    MyProblem(@Nullable final URI type, @Nullable final String title) {
        super(type, title);
    }

    MyProblem(@Nullable final URI type, @Nullable final String title,
            @Nullable final Response.StatusType status) {
        super(type, title, status);
    }

    MyProblem(@Nullable final URI type, @Nullable final String title,
            @Nullable final Response.StatusType status, @Nullable final String detail) {
        super(type, title, status, detail);
    }

    MyProblem(@Nullable final URI type, @Nullable final String title,
            @Nullable final Response.StatusType status, @Nullable final String detail,
            @Nullable final URI instance) {
        super(type, title, status, detail, instance);
    }

    MyProblem(@Nullable final URI type, @Nullable final String title,
            @Nullable final Response.StatusType status, @Nullable final String detail,
            @Nullable final URI instance, @Nullable final ThrowableProblem cause) {
        super(type, title, status, detail, instance, cause);
    }

    MyProblem(@Nullable final URI type, @Nullable final String title,
            @Nullable final Response.StatusType status, @Nullable final String detail,
            @Nullable final URI instance, @Nullable final ThrowableProblem cause,
            @Nullable final Map<String, Object> parameters) {
        super(type, title, status, detail, instance, cause, parameters);
    }

}
