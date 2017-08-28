package org.zalando.problem;

import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response.Status;

import static org.junit.jupiter.api.Assertions.assertThrows;

final class ProblemModuleTest {

    @Test
    void defaultConstructorShouldBuildIndexCorrectly() {
        new ProblemModule();
    }

    @Test
    void shouldThrowForDuplicateStatusCode() {
        assertThrows(IllegalArgumentException.class, () -> new ProblemModule(Status.class, CustomStatus.class));
    }

}
