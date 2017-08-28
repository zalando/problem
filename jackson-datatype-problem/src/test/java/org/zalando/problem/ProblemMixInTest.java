package org.zalando.problem;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import static com.jayway.jsonassert.JsonAssert.with;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;
import static org.hamcrest.MatcherAssert.assertThat;

final class ProblemMixInTest {

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new ProblemModule());

    ProblemMixInTest() {
        mapper.registerSubtypes(InsufficientFundsProblem.class);
        mapper.registerSubtypes(OutOfStockException.class);
    }

    @Test
    void shouldSerializeDefaultProblem() throws JsonProcessingException {
        final Problem problem = Problem.valueOf(Status.NOT_FOUND);
        final String json = mapper.writeValueAsString(problem);

        with(json)
                .assertThat("$.*", hasSize(2))
                .assertThat("$.title", is("Not Found"))
                .assertThat("$.status", is(404));
    }

    @Test
    void shouldSerializeCustomProperties() throws JsonProcessingException {
        final Problem problem = Problem.builder()
                .withType(URI.create("https://example.org/out-of-stock"))
                .withTitle("Out of Stock")
                .withStatus(BAD_REQUEST)
                .withDetail("Item B00027Y5QG is no longer available")
                .with("product", "B00027Y5QG")
                .build();

        final String json = mapper.writeValueAsString(problem);

        with(json)
                .assertThat("$.*", hasSize(5))
                .assertThat("$.product", is("B00027Y5QG"));
    }

    @Test
    void shouldSerializeProblemCause() throws JsonProcessingException {
        final Problem problem = Problem.builder()
                .withType(URI.create("https://example.org/preauthorization-failed"))
                .withTitle("Preauthorization Failed")
                .withStatus(BAD_REQUEST)
                .withCause(Problem.builder()
                        .withType(URI.create("https://example.org/expired-credit-card"))
                        .withTitle("Expired Credit Card")
                        .withStatus(BAD_REQUEST)
                        .withDetail("Credit card is expired as of 2015-09-16T00:00:00Z")
                        .with("since", "2015-09-16T00:00:00Z")
                        .build())
                .build();

        final String json = mapper.writeValueAsString(problem);

        with(json)
                .assertThat("$.cause.type", is("https://example.org/expired-credit-card"))
                .assertThat("$.cause.title", is("Expired Credit Card"))
                .assertThat("$.cause.status", is(400))
                .assertThat("$.cause.detail", is("Credit card is expired as of 2015-09-16T00:00:00Z"))
                .assertThat("$.cause.since", is("2015-09-16T00:00:00Z"));
    }

    @Test
    void shouldNotSerializeStacktraceByDefault() throws JsonProcessingException {
        final Problem problem = Problem.builder()
                .withType(URI.create("about:blank"))
                .withTitle("Foo")
                .withStatus(BAD_REQUEST)
                .withCause(Problem.builder()
                        .withType(URI.create("about:blank"))
                        .withTitle("Bar")
                        .withStatus(BAD_REQUEST)
                        .build())
                .build();

        final String json = mapper.writeValueAsString(problem);

        with(json)
                .assertNotDefined("$.stacktrace");
    }

    @Test
    void shouldSerializeStacktrace() throws JsonProcessingException {
        final Problem problem = Problem.builder()
                .withType(URI.create("about:blank"))
                .withTitle("Foo")
                .withStatus(BAD_REQUEST)
                .withCause(Problem.builder()
                        .withType(URI.create("about:blank"))
                        .withTitle("Bar")
                        .withStatus(BAD_REQUEST)
                        .build())
                .build();

        final ObjectMapper mapper = new ObjectMapper()
                .registerModule(new ProblemModule().withStackTraces());

        final String json = mapper.writeValueAsString(problem);

        with(json)
                .assertThat("$.stacktrace", is(instanceOf(List.class)))
                .assertThat("$.stacktrace[0]", is(instanceOf(String.class)));
    }

    @Test
    void shouldDeserializeDefaultProblem() throws IOException {
        final URL resource = getResource("default.json");
        final Problem raw = mapper.readValue(resource, Problem.class);

        assertThat(raw, instanceOf(DefaultProblem.class));
        final DefaultProblem problem = (DefaultProblem) raw;

        assertThat(problem, hasFeature("type", Problem::getType, hasToString("https://example.org/not-out-of-stock")));
        assertThat(problem, hasFeature("title", Problem::getTitle, equalTo("Out of Stock")));
        assertThat(problem, hasFeature("status", Problem::getStatus, equalTo(BAD_REQUEST)));
        assertThat(problem, hasFeature("detail", Problem::getDetail, is("Item B00027Y5QG is no longer available")));
        assertThat(problem, hasFeature("parameters", DefaultProblem::getParameters, hasEntry("product", "B00027Y5QG")));
    }

    @Test
    void shouldDeserializeRegisteredExceptional() throws IOException {
        final URL resource = getResource("out-of-stock.json");
        final Exceptional exceptional = mapper.readValue(resource, Exceptional.class);

        assertThat(exceptional, instanceOf(OutOfStockException.class));
        final OutOfStockException problem = (OutOfStockException) exceptional;

        assertThat(problem, hasFeature("type", Problem::getType, hasToString("https://example.org/out-of-stock")));
        assertThat(problem, hasFeature("title", Problem::getTitle, equalTo("Out of Stock")));
        assertThat(problem, hasFeature("status", Problem::getStatus, equalTo(BAD_REQUEST)));
        assertThat(problem, hasFeature("detail", Problem::getDetail, is("Item B00027Y5QG is no longer available")));
    }

    @Test
    void shouldDeserializeUnregisteredExceptional() throws IOException {
        final URL resource = getResource("out-of-stock.json");
        final Exceptional exceptional = mapper.readValue(resource, IOProblem.class);

        assertThat(exceptional, instanceOf(IOProblem.class));
        final IOProblem problem = (IOProblem) exceptional;

        assertThat(problem, hasFeature("type", Problem::getType, hasToString("https://example.org/out-of-stock")));
        assertThat(problem, hasFeature("title", Problem::getTitle, equalTo("Out of Stock")));
        assertThat(problem, hasFeature("status", Problem::getStatus, equalTo(BAD_REQUEST)));
        assertThat(problem, hasFeature("detail", Problem::getDetail, is("Item B00027Y5QG is no longer available")));
    }

    @Test
    void shouldDeserializeSpecificProblem() throws IOException {
        final URL resource = getResource("insufficient-funds.json");
        final InsufficientFundsProblem problem = (InsufficientFundsProblem) mapper.readValue(resource, Problem.class);

        assertThat(problem, hasFeature("balance", InsufficientFundsProblem::getBalance, equalTo(10)));
        assertThat(problem, hasFeature("debit", InsufficientFundsProblem::getDebit, equalTo(-20)));
    }

    @Test
    void shouldDeserializeUnknownStatus() throws IOException {
        final URL resource = getResource("unknown.json");
        final Problem problem = mapper.readValue(resource, Problem.class);

        final StatusType status = problem.getStatus();

        assertThat(status, hasFeature("status code", StatusType::getStatusCode, equalTo(666)));
        assertThat(status, hasFeature("family", StatusType::getFamily, equalTo(Family.OTHER)));
        assertThat(status, hasFeature("reason phrase", StatusType::getReasonPhrase, equalTo("Unknown")));
    }

    @Test
    void shouldDeserializeUntyped() throws IOException {
        final URL resource = getResource("untyped.json");
        final Problem problem = mapper.readValue(resource, Problem.class);

        assertThat(problem.getType(), hasToString("about:blank"));
        assertThat(problem.getTitle(), is("Something bad"));
        assertThat(problem.getStatus(), hasFeature(StatusType::getStatusCode, is(400)));
        assertThat(problem.getDetail(), is(nullValue()));
        assertThat(problem.getInstance(), is(nullValue()));
    }

    @Test
    void shouldDeserializeEmpty() throws IOException {
        final URL resource = getResource("empty.json");
        final Problem problem = mapper.readValue(resource, Problem.class);

        assertThat(problem.getType(), hasToString("about:blank"));
        assertThat(problem.getTitle(), is(nullValue()));
        assertThat(problem.getStatus(), is(nullValue()));
        assertThat(problem.getDetail(), is(nullValue()));
        assertThat(problem.getInstance(), is(nullValue()));
    }

    @Test
    void shouldDeserializeCause() throws IOException {
        final URL resource = getResource("cause.json");
        final ThrowableProblem problem = mapper.readValue(resource, ThrowableProblem.class);

        assertThat(problem, hasFeature("cause", Throwable::getCause, is(notNullValue())));
        final DefaultProblem cause = (DefaultProblem) problem.getCause();

        assertThat(cause, is(notNullValue()));
        assertThat(cause, instanceOf(DefaultProblem.class));

        assertThat(cause, hasFeature("type", Problem::getType, hasToString("https://example.org/expired-credit-card")));
        assertThat(cause, hasFeature("title", Problem::getTitle, equalTo("Expired Credit Card")));
        assertThat(cause, hasFeature("status", Problem::getStatus, equalTo(BAD_REQUEST)));
        assertThat(cause, hasFeature("detail", Problem::getDetail, is("Credit card is expired as of 2015-09-16T00:00:00Z")));
        assertThat(cause, hasFeature("parameters", Problem::getParameters, hasEntry("since", "2015-09-16T00:00:00Z")));
    }

    @Test
    void shouldDeserializeWithProcessedStackTrace() throws IOException {
        final URL resource = getResource("cause.json");
        final ThrowableProblem problem = mapper.readValue(resource, ThrowableProblem.class);

        final String stackTrace = getStackTrace(problem);
        final String[] stackTraceElements = stackTrace.split("\n");

        assertThat(stackTraceElements[1], startsWith("\tat org.zalando.problem.ProblemMixInTest"));
    }

    private String getStackTrace(final Throwable throwable) {
        final StringWriter writer = new StringWriter();
        throwable.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    private static URL getResource(final String name) {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        return Objects.requireNonNull(loader.getResource(name), () -> "resource " + name + " not found.");
    }

}
