package org.zalando.problem;

import org.junit.Test;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;
import static org.junit.Assert.assertThat;

public final class MoreStatusTest {

    @Test
    public void shouldBeDistinctFromStatus() {
        Stream.of(MoreStatus.values()).map(StatusType::getStatusCode).forEach(code -> {
            final Status status = Status.fromStatusCode(code);
            assertThat("Duplicate code: " + code, status, is(nullValue()));
        });
    }

    @Test
    public void shouldHaveFamily() {
        Stream.of(MoreStatus.values()).forEach(status ->
                assertThat(status, hasFeature("family", StatusType::getFamily, notNullValue())));
    }

    @Test
    public void shouldHaveReasonPhrase() {
        Stream.of(MoreStatus.values()).forEach(status ->
                assertThat(status, hasFeature("reason phrase", StatusType::getReasonPhrase, not(isEmptyOrNullString()))));
    }

    @Test
    public void shouldFindByStatusCode() {
        assertThat(MoreStatus.fromStatusCode(422), is(notNullValue()));
    }

    @Test
    public void shouldNotFindByStatusCode() {
        assertThat(MoreStatus.fromStatusCode(600), is(nullValue()));
    }

}