package org.zalando.problem;

import org.junit.Test;

import java.net.URI;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;
import static org.junit.Assert.assertThat;

public class ProblemBuilderTest {

    private final URI type = URI.create("https://example.org/out-of-stock");

    @Test
    public void shouldCreateEmptyProblem() {
        final Problem problem = Problem.builder().build();

        assertThat(problem, hasFeature("type", Problem::getType, hasToString("about:blank")));
        assertThat(problem, hasFeature("title", Problem::getTitle, is(nullValue())));
        assertThat(problem, hasFeature("status", Problem::getStatus, is(nullValue())));
        assertThat(problem, hasFeature("detail", Problem::getDetail, is(nullValue())));
        assertThat(problem, hasFeature("instance", Problem::getInstance, is(nullValue())));
    }

    @Test
    public void shouldCreateProblem() {
        final Problem problem = Problem.builder()
                .withType(type)
                .withTitle("Out of Stock")
                .withStatus(BAD_REQUEST)
                .build();

        assertThat(problem, hasFeature("type", Problem::getType, is(type)));
        assertThat(problem, hasFeature("title", Problem::getTitle, is("Out of Stock")));
        assertThat(problem, hasFeature("status", Problem::getStatus, is(BAD_REQUEST)));
        assertThat(problem, hasFeature("detail", Problem::getDetail, is(nullValue())));
        assertThat(problem, hasFeature("instance", Problem::getInstance, is(nullValue())));
    }

    @Test
    public void shouldCreateProblemWithDetail() {
        final Problem problem = Problem.builder()
                .withType(type)
                .withTitle("Out of Stock")
                .withStatus(BAD_REQUEST)
                .withDetail("Item B00027Y5QG is no longer available")
                .build();

        assertThat(problem, hasFeature("detail", Problem::getDetail, is("Item B00027Y5QG is no longer available")));
    }

    @Test
    public void shouldCreateProblemWithInstance() {
        final Problem problem = Problem.builder()
                .withType(type)
                .withTitle("Out of Stock")
                .withStatus(BAD_REQUEST)
                .withInstance(URI.create("https://example.com/"))
                .build();

        assertThat(problem, hasFeature("instance", Problem::getInstance, is(URI.create("https://example.com/"))));
    }

    @Test
    public void shouldCreateProblemWithParameters() {
        final ThrowableProblem problem = Problem.builder()
                .withType(type)
                .withTitle("Out of Stock")
                .withStatus(BAD_REQUEST)
                .with("foo", "bar")
                .build();

        assertThat(problem.getParameters(), hasEntry("foo", "bar"));
    }
    
    @Test
    public void shouldCreateProblemWithCause() {
        final ThrowableProblem problem = Problem.builder()
                .withType(URI.create("https://example.org/preauthorization-failed"))
                .withTitle("Preauthorization Failed")
                .withStatus(BAD_REQUEST)
                .withCause(Problem.builder()
                        .withType(URI.create("https://example.org/expired-credit-card"))
                        .withTitle("Expired Credit Card")
                        .withStatus(BAD_REQUEST)
                        .build())
                .build();
        
        assertThat(problem, hasFeature("cause", ThrowableProblem::getCause, notNullValue()));

        final ThrowableProblem cause = problem.getCause();
        assertThat(cause, hasFeature("type", Problem::getType, hasToString("https://example.org/expired-credit-card")));
        assertThat(cause, hasFeature("title", Problem::getTitle, is("Expired Credit Card")));
        assertThat(cause, hasFeature("status", Problem::getStatus, is(BAD_REQUEST)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnCustomType() {
        Problem.builder().with("type", "foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnCustomTitle() {
        Problem.builder().with("title", "foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnCustomStatus() {
        Problem.builder().with("status", "foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnCustomDetail() {
        Problem.builder().with("detail", "foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnCustomInstance() {
        Problem.builder().with("instance", "foo");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowOnCustomCause() {
        Problem.builder().with("cause", "foo");
    }

}