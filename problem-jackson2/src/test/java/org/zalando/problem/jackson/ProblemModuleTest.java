package org.zalando.problem.jackson;

import org.junit.jupiter.api.Test;
import org.zalando.problem.Status;

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
