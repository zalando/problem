package org.zalando.problem;

import org.zalando.problem.spi.StackTraceProcessor;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

public final class JunitStackTraceProcessor implements StackTraceProcessor {

    @Override
    public Collection<StackTraceElement> process(final Collection<StackTraceElement> elements) {
        return elements.stream()
                .filter(element -> !element.getClassName().startsWith("org.junit"))
                .collect(toList());
    }

}
