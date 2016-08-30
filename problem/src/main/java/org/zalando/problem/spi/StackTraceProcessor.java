package org.zalando.problem.spi;

import java.util.Collection;

import static java.util.ServiceLoader.load;
import static java.util.stream.StreamSupport.stream;

/**
 * @see java.util.ServiceLoader
 */
public interface StackTraceProcessor {

    StackTraceProcessor DEFAULT = elements -> elements;
    StackTraceProcessor COMPOUND = stream(load(StackTraceProcessor.class).spliterator(), false)
            .reduce(DEFAULT, (first, second) -> elements -> second.process(first.process(elements)));

    Collection<StackTraceElement> process(final Collection<StackTraceElement> elements);

}
