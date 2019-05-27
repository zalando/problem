package org.zalando.problem;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    @ParameterizedTest
    @CsvSource({
            "409, Conflict",
            "404, Not Found",
            "200, OK",
            "500, Internal Server Error"
    })
    void shouldHaveCorrectValueFromCode(int code, String line) {
        Status statusFromCode = Status.ofCode(code);

        assertThat(statusFromCode.getStatusCode(), equalTo(code));
        assertThat(statusFromCode.getReasonPhrase(), equalTo(line));
    }

    @Test
    void shouldThrowOnNonExistingCode() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Status.ofCode(111);
        });
    }
}
