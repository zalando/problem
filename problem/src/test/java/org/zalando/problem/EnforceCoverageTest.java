package org.zalando.problem;

import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.zalando.problem.Status.BAD_REQUEST;

class EnforceCoverageTest {

    @Test
    void shouldCoverUnreachableThrowStatement() throws Exception {
        assertThrows(FakeProblem.class, () -> {
            throw new FakeProblem().propagate();
        });
    }

    static final class FakeProblem extends Exception implements Exceptional {

        @Override
        public URI getType() {
            return URI.create("about:blank");
        }

        @Override
        public String getTitle() {
            return "Fake";
        }

        @Override
        public StatusType getStatus() {
            return BAD_REQUEST;
        }

        @Override
        public ThrowableProblem getCause() {
            return null;
        }

        @Override
        public <X extends Throwable> X propagateAs(final Class<X> type) throws X {
            return type.cast(this);
        }

    }

}
