package org.zalando.problem;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static org.zalando.problem.spi.StackTraceProcessor.COMPOUND;

@Immutable
public abstract class ThrowableProblem extends RuntimeException implements Problem, Exceptional {

    public ThrowableProblem() {
        this(null);
    }

    public ThrowableProblem(@Nullable final ThrowableProblem cause) {
        super(cause);

        final Collection<StackTraceElement> stackTrace = COMPOUND.process(asList(getStackTrace()));
        setStackTrace(stackTrace.toArray(new StackTraceElement[stackTrace.size()]));
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
