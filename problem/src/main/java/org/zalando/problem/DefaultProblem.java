package org.zalando.problem;

import org.apiguardian.api.API;

import javax.annotation.Nullable;
import java.net.URI;
import java.util.Map;

import static org.apiguardian.api.API.Status.STABLE;

@API(status = STABLE)
public final class DefaultProblem extends AbstractThrowableProblem {

    // TODO needed for jackson
    DefaultProblem(@Nullable final URI type,
            @Nullable final String title,
            @Nullable final StatusType status,
            @Nullable final String detail,
            @Nullable final URI instance,
            @Nullable final ThrowableProblem cause) {
        super(type, title, status, detail, instance, cause);
    }

    DefaultProblem(@Nullable final URI type,
            @Nullable final String title,
            @Nullable final StatusType status,
            @Nullable final String detail,
            @Nullable final URI instance,
            @Nullable final ThrowableProblem cause,
            @Nullable final Map<String, Object> parameters) {
        super(type, title, status, detail, instance, cause, parameters);
    }
}
