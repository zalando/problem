package org.zalando.problem;

import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response.Status;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;
import static org.hamcrest.MatcherAssert.assertThat;

final class ProblemStaticFactoryTest {

    @Test
    void shouldCreateGenericProblem() {
        final Problem problem = Problem.valueOf(Status.NOT_FOUND);

        assertThat(problem, hasFeature("type", Problem::getType, hasToString("about:blank")));
        assertThat(problem, hasFeature("title", Problem::getTitle, equalTo("Not Found")));
        assertThat(problem, hasFeature("status", Problem::getStatus, equalTo(Status.NOT_FOUND)));
    }

    @Test
    void shouldCreateGenericProblemWithDetail() {
        final Problem problem = Problem.valueOf(Status.NOT_FOUND, "Order 123");

        assertThat(problem, hasFeature("type", Problem::getType, hasToString("about:blank")));
        assertThat(problem, hasFeature("title", Problem::getTitle, equalTo("Not Found")));
        assertThat(problem, hasFeature("status", Problem::getStatus, equalTo(Status.NOT_FOUND)));
        assertThat(problem, hasFeature("detail", Problem::getDetail, is("Order 123")));
    }

}
