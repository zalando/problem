package org.zalando.problem;

import com.google.gag.annotation.remark.Hack;
import com.google.gag.annotation.remark.OhNoYouDidnt;
import org.junit.Test;

import java.net.URI;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Hack
@OhNoYouDidnt
public final class EnforceCoverageTest {

    @Test(expected = AbstractThrowableProblem.class)
    public void shouldUseMixinConstructor() {
        new AbstractThrowableProblemMixIn(URI.create("https://example.org"), "Bad Request", BAD_REQUEST, null, null, null) {

            @Override
            void set(final String key, final Object value) {

            }

        };
    }

}