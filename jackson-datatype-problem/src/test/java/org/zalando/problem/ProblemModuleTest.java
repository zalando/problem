package org.zalando.problem;

import org.junit.jupiter.api.Test;

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
