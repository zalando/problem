package org.zalando.problem.jackson;

import org.junit.jupiter.api.Test;
import org.zalando.problem.AbstractThrowableProblem;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.zalando.problem.Status.BAD_REQUEST;

final class EnforceCoverageTest {

    @Test
    void shouldUseMixinConstructor() {
        assertThrows(AbstractThrowableProblem.class, () -> {
            final URI type = URI.create("https://example.org");
            new AbstractThrowableProblemMixIn(type, "Bad Request", BAD_REQUEST, null, null, null) {
                @Override
                void set(final String key, final Object value) {
                    // not used within this test
                }
            };
        });
    }

}
