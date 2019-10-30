package org.zalando.problem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UnknownStatusTest {

    @Test
    void shouldReturnCodeAndPhrase() {
        final int code = 8080;
        final UnknownStatus status = new UnknownStatus(code);
        assertEquals(8080, status.getStatusCode());
        assertEquals("Unknown", status.getReasonPhrase());
    }

}