package org.zalando.problem;

import com.google.gag.annotation.remark.Hack;
import com.google.gag.annotation.remark.OhNoYouDidnt;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.zalando.problem.Status.BAD_REQUEST;

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
