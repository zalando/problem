package org.zalando.problem;

import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;
import static org.hamcrest.MatcherAssert.assertThat;

final class MoreStatusTest {

    @Test
    void shouldBeDistinctFromStatus() {
        Stream.of(MoreStatus.values()).map(StatusType::getStatusCode).forEach(code -> {
            final Status status = Status.fromStatusCode(code);
            assertThat("Duplicate code: " + code, status, is(nullValue()));
        });
    }

    @Test
    void shouldHaveFamily() {
        Stream.of(MoreStatus.values()).forEach(status ->
                assertThat(status, hasFeature("family", StatusType::getFamily, notNullValue())));
    }

    @Test
    void shouldHaveReasonPhrase() {
        Stream.of(MoreStatus.values()).forEach(status ->
                assertThat(status, hasFeature("reason phrase", StatusType::getReasonPhrase, is(not(emptyOrNullString())))));
    }

    @Test
    void shouldFindByStatusCode() {
        assertThat(MoreStatus.fromStatusCode(422), is(notNullValue()));
    }

    @Test
    void shouldNotFindByStatusCode() {
        assertThat(MoreStatus.fromStatusCode(600), is(nullValue()));
    }

}
