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
    void shouldCreateProblemWithNullParameter() {
        final ThrowableProblem problem = Problem.builder()
                .withType(type)
                .withTitle("Out of Stock")
                .withStatus(BAD_REQUEST)
                .with("foo", "will-be-overridden")
                .with("foo", null)
                .build();

        assertThat(problem.getParameters(), hasEntry("foo", null));
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

    @Test
    void shouldCreateBuilderFromExistingProblem() {
        final Problem original = Problem.builder()
                .withType(type)
                .withTitle("Out of Stock")
                .withStatus(BAD_REQUEST)
                .withDetail("Item B00027Y5QG is no longer available")
                .withInstance(URI.create("https://example.com/"))
                .build();

        final Problem copy = original.toBuilder().build();

        assertThat(copy, hasFeature("type", Problem::getType, is(type)));
        assertThat(copy, hasFeature("title", Problem::getTitle, is("Out of Stock")));
        assertThat(copy, hasFeature("status", Problem::getStatus, is(BAD_REQUEST)));
        assertThat(copy, hasFeature("detail", Problem::getDetail, is("Item B00027Y5QG is no longer available")));
        assertThat(copy, hasFeature("instance", Problem::getInstance, is(URI.create("https://example.com/"))));
    }

    @Test
    void shouldCopyParametersFromExistingProblem() {
        final Problem original = Problem.builder()
                .withType(type)
                .withStatus(BAD_REQUEST)
                .with("traceId", "abc-123")
                .with("region", "eu-west-1")
                .build();

        final Problem copy = original.toBuilder().build();

        assertThat(copy.getParameters(), hasEntry("traceId", "abc-123"));
        assertThat(copy.getParameters(), hasEntry("region", "eu-west-1"));
    }

    @Test
    void shouldAllowAddingNewParametersViaToBuilder() {
        final Problem original = Problem.builder()
                .withType(type)
                .withStatus(BAD_REQUEST)
                .with("existingKey", "existingValue")
                .build();

        // primary use case — add a trace ID to any problem response
        final Problem enriched = original.toBuilder()
                .with("traceId", "xyz-789")
                .build();

        assertThat(enriched.getParameters(), hasEntry("existingKey", "existingValue"));
        assertThat(enriched.getParameters(), hasEntry("traceId", "xyz-789"));
    }

    @Test
    void shouldAllowOverridingFieldsViaToBuilder() {
        final Problem original = Problem.builder()
                .withType(type)
                .withStatus(BAD_REQUEST)
                .withDetail("original detail")
                .build();

        final Problem modified = original.toBuilder()
                .withDetail("updated detail")
                .build();

        assertThat(modified, hasFeature("detail", Problem::getDetail, is("updated detail")));
        assertThat(modified, hasFeature("type", Problem::getType, is(type)));
        assertThat(modified, hasFeature("status", Problem::getStatus, is(BAD_REQUEST)));
    }

    @Test
    void toBuilderOnEmptyProblemShouldProduceEquivalentProblem() {
        final Problem original = Problem.builder().build();
        final Problem copy = original.toBuilder().build();

        assertThat(copy, hasFeature("type", Problem::getType, hasToString("about:blank")));
        assertThat(copy, hasFeature("title", Problem::getTitle, is(nullValue())));
        assertThat(copy, hasFeature("status", Problem::getStatus, is(nullValue())));
        assertThat(copy, hasFeature("detail", Problem::getDetail, is(nullValue())));
        assertThat(copy, hasFeature("instance", Problem::getInstance, is(nullValue())));
    }

}
