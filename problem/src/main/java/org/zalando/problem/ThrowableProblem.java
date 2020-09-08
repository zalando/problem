package org.zalando.problem;

import org.apiguardian.api.API;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static org.apiguardian.api.API.Status.STABLE;
import static org.zalando.problem.spi.StackTraceProcessor.COMPOUND;

/**
 * 
 * {@link Problem} instances are required to be immutable.
 */
@API(status = STABLE)
public abstract class ThrowableProblem extends RuntimeException implements Problem, Exceptional {

    protected ThrowableProblem() {
        this(null);
    }

    protected ThrowableProblem(@Nullable final ThrowableProblem cause) {
        super(cause);

        final Collection<StackTraceElement> stackTrace = COMPOUND.process(asList(getStackTrace()));
        setStackTrace(stackTrace.toArray(new StackTraceElement[0]));
    }

    @Override
    public String getMessage() {
        return Stream.of(getTitle(), getDetail())
            .filter(Objects::nonNull)
            .collect(joining(": "));
    }

    @Override
    public ThrowableProblem getCause() {
        // cast is safe, since the only way to set this is our constructor
        return (ThrowableProblem) super.getCause();
    }

    @Override
    public String toString() {
        return Problem.toString(this);
    }

}
