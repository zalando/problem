package org.zalando.problem;

import org.junit.Test;

public final class GenericProblemsTest {

    @Test(expected = Exception.class)
    public void shouldNotBeInstantiable() throws Exception {
        new GenericProblems();
    }

}