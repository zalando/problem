package org.zalando.problem;

import org.junit.Test;

import javax.ws.rs.core.Response.Status;

public final class ProblemModuleTest {

    @Test
    public void defaultConstructorShouldBuildIndexCorrectly() {
        new ProblemModule();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowForDuplicateStatusCode() {
        new ProblemModule(Status.class, CustomStatus.class);
    }

}
