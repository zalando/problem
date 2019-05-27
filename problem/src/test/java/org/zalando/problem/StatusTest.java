package org.zalando.problem;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;
import static org.junit.jupiter.api.Assertions.assertThrows;


final class StatusTest {

    @Test
    void shouldHaveReasonPhrase() {
        Stream.of(Status.values()).forEach(status ->
                assertThat(status, hasFeature("reason phrase", StatusType::getReasonPhrase, is(not(emptyOrNullString())))));
    }

    @Test
    void shouldHaveMeaningfulToString() {
        assertThat(Status.NOT_FOUND.toString(), equalTo("404 Not Found"));
    }

    @ParameterizedTest
    @CsvSource({
            "409, Conflict",
            "404, Not Found",
            "200, OK",
            "500, Internal Server Error"
    })
    void shouldHaveCorrectValueFromCode(final int statusCode, final String reasonPhrase) {
        final Status status = Status.valueOf(statusCode);

        assertThat(status.getStatusCode(), equalTo(statusCode));
        assertThat(status.getReasonPhrase(), equalTo(reasonPhrase));
    }

    @Test
    void shouldThrowOnNonExistingCode() {
        assertThrows(IllegalArgumentException.class, () -> Status.valueOf(111));
    }

}
