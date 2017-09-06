package org.zalando.problem;

import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.zalando.problem.Status.BAD_REQUEST;

class ProblemBuilderTest {

    private final URI type = URI.create("https://example.org/out-of-stock");

    @Test
    void shouldCreateEmptyProblem() {
        final Problem problem = Problem.builder().build();

        assertThat(problem, hasFeature("type", Problem::getType, hasToString("about:blank")));
        assertThat(problem, hasFeature("title", Problem::getTitle, is(nullValue())));
        assertThat(problem, hasFeature("status", Problem::getStatus, is(nullValue())));
        assertThat(problem, hasFeature("detail", Problem::getDetail, is(nullValue())));
        assertThat(problem, hasFeature("instance", Problem::getInstance, is(nullValue())));
    }

    @Test
    void shouldCreateProblem() {
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
    void shouldCreateProblemWithDetail() {
        final Problem problem = Problem.builder()
                .withType(type)
                .withTitle("Out of Stock")
                .withStatus(BAD_REQUEST)
                .withDetail("Item B00027Y5QG is no longer available")
                .build();

        assertThat(problem, hasFeature("detail", Problem::getDetail, is("Item B00027Y5QG is no longer available")));
    }

    @Test
    void shouldCreateProblemWithInstance() {
        final Problem problem = Problem.builder()
                .withType(type)
                .withTitle("Out of Stock")
                .withStatus(BAD_REQUEST)
                .withInstance(URI.create("https://example.com/"))
                .build();

        assertThat(problem, hasFeature("instance", Problem::getInstance, is(URI.create("https://example.com/"))));
    }

    @Test
    void shouldCreateProblemWithParameters() {
        final ThrowableProblem problem = Problem.builder()
                .withType(type)
                .withTitle("Out of Stock")
                .withStatus(BAD_REQUEST)
                .with("foo", "bar")
                .build();

        assertThat(problem.getParameters(), hasEntry("foo", "bar"));
    }
    
    @Test
    void shouldCreateProblemWithCause() {
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

    @Test
    void shouldThrowOnCustomType() {
        assertThrows(IllegalArgumentException.class, () -> Problem.builder().with("type", "foo"));
    }

    @Test
    void shouldThrowOnCustomTitle() {
        assertThrows(IllegalArgumentException.class, () -> Problem.builder().with("title", "foo"));
    }

    @Test
    void shouldThrowOnCustomStatus() {
        assertThrows(IllegalArgumentException.class, () -> Problem.builder().with("status", "foo"));
    }

    @Test
    void shouldThrowOnCustomDetail() {
        assertThrows(IllegalArgumentException.class, () -> Problem.builder().with("detail", "foo"));
    }

    @Test
    void shouldThrowOnCustomInstance() {
        assertThrows(IllegalArgumentException.class, () -> Problem.builder().with("instance", "foo"));
    }
    
    @Test
    void shouldThrowOnCustomCause() {
        assertThrows(IllegalArgumentException.class, () -> Problem.builder().with("cause", "foo"));
    }

}
