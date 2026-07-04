package org.zalando.problem;

import java.util.Collection;

import org.zalando.problem.spi.StackTraceProcessor;

import static java.util.stream.Collectors.toList;

public final class JunitStackTraceProcessor implements StackTraceProcessor {

    @Override
    public Collection<StackTraceElement> process(final Collection<StackTraceElement> elements) {
        return elements.stream()
                .filter(element -> !isJunitStackTrace(element))
                .collect(toList());
    }

    /**
     * Determines whether a stack trace element originates from JUnit.
     *
     * @param element the stack trace element to inspect
     * @return {@code true} if the element belongs to a JUnit class or module;
     *         {@code false} otherwise
     */
    private boolean isJunitStackTrace(final StackTraceElement element) {
        final String moduleName = element.getModuleName();

        return element.getClassName().startsWith("org.junit.")
                || (moduleName != null
                && moduleName.startsWith("org.junit."));
    }

}