package org.zalando.problem;

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import org.zalando.problem.spi.StackTraceProcessor;

public final class JunitStackTraceProcessor implements StackTraceProcessor {

    @Override
    public Collection<StackTraceElement> process(
        final Collection<StackTraceElement> elements
    ) {
        return elements
            .stream()
            .filter(element -> !isJunitStackTrace(element))
            .collect(toList());
    }

    private boolean isJunitStackTrace(final StackTraceElement element) {
        final String className = element.getClassName();
        // Filter by class name - catch all JUnit packages
        if (className.startsWith("org.junit.")) {
            return true;
        }
        // Filter by module name (Java 9+)
        final String moduleName = element.getModuleName();
        if (moduleName != null && moduleName.startsWith("org.junit.")) {
            return true;
        }
        return false;
    }
}
