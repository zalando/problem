package org.zalando.problem;

import com.google.gag.annotation.remark.Hack;
import com.google.gag.annotation.remark.OhNoYouDidnt;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.net.URI;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Hack
@OhNoYouDidnt
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
        public Response.StatusType getStatus() {
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
