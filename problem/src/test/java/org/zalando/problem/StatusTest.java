package org.zalando.problem;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;


final class StatusTest {

    @Test
    void shouldHaveReasonPhrase() {
        Stream.of(Status.values()).forEach(status ->
                assertThat(status, hasFeature("reason phrase", StatusType::getReasonPhrase, is(not(emptyOrNullString())))));
    }

    @Test
    void shouldHaveMeaningfulToString() {
        Status notFound = Status.NOT_FOUND;

        assertThat(notFound.toString(), equalTo("404 Not Found"));
    }
}
