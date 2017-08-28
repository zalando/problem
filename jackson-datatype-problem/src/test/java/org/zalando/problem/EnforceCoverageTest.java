package org.zalando.problem;

import com.google.gag.annotation.remark.Hack;
import com.google.gag.annotation.remark.OhNoYouDidnt;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Hack
@OhNoYouDidnt
final class EnforceCoverageTest {

    @Test
    void shouldUseMixinConstructor() {
        assertThrows(AbstractThrowableProblem.class, () -> {
            new AbstractThrowableProblemMixIn(URI.create("https://example.org"), "Bad Request", BAD_REQUEST, null, null,
                    null) {

                @Override
                void set(final String key, final Object value) {

                }

            };
        });
    }

}
