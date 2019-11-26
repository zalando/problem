package org.zalando.problem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.MatcherAssert.assertThat;
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
import static org.zalando.problem.Status.BAD_REQUEST;

class GsonProblemTest {

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapterFactory(new ProblemAdapterFactory()
                    .registerSubtype(InsufficientFundsProblem.class, InsufficientFundsProblem.TYPE)
                    .registerSubtype(OutOfStockException.class, OutOfStockException.TYPE))
            .create();

    private static JsonReader getReader(final String name) throws FileNotFoundException {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL resource = Objects.requireNonNull(loader.getResource(name), () -> "resource " + name + " not found.");
        return new JsonReader(new FileReader(new File(resource.getPath())));
    }

    private String getStackTrace(final Throwable throwable) {
        final StringWriter writer = new StringWriter();
        throwable.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

    @Test
    void shouldSerializeDefaultProblem() {
        final Problem problem = Problem.valueOf(Status.NOT_FOUND);
        final String json = gson.toJson(problem);
        assertThat("json", Objects.nonNull(json));
        with(json)
                .assertThat("$.*", hasSize(2))
                .assertThat("$.title", is("Not Found"))
                .assertThat("$.status", is(404));
    }

    @Test
    void shouldSerializeCustomProperties() {
        final Problem problem = Problem.builder()
                .withType(URI.create("https://example.org/out-of-stock"))
                .withTitle("Out of Stock")
                .withStatus(BAD_REQUEST)
                .withDetail("Item B00027Y5QG is no longer available")
                .with("product", "B00027Y5QG")
                .build();

        final String json = gson.toJson(problem);
        assertThat("json", Objects.nonNull(json));
        with(json)
                .assertThat("$.*", hasSize(5))
                .assertThat("$.product", is("B00027Y5QG"));
    }

    @Test
    void shouldSerializeCustomProblem() {
        final int balance = 10;
        final int debit = 100;
        final InsufficientFundsProblem problem = new InsufficientFundsProblem(balance, debit);
        problem.set("foo", "bar");

        final String json = gson.toJson(problem);
        assertThat("json", Objects.nonNull(json));
        System.out.println(json);
        with(json)
                .assertThat("$.*", hasSize(6))
                .assertThat("$.type", is(InsufficientFundsProblem.TYPE_VALUE))
                .assertThat("$.title", is("Insufficient Funds"))
                .assertThat("$.status", is(BAD_REQUEST.getStatusCode()))
                .assertThat("$.foo", is("bar"))
                .assertThat("$.balance", is(balance))
                .assertThat("$.debit", is(debit));
    }

    @Test
    void shouldSerializeUnregisteredCustomProblem() {
        final URI type = URI.create("https://example.org/io-problem");
        final String title = "Storage Overflow";
        final StatusType status = Status.INSUFFICIENT_STORAGE;
        final String detail = "IO Problem Encountered";
        final URI instance = URI.create("https://example.org");

        final Problem problem = new IOProblem(type, title, status, detail, instance);

        final String json = gson.toJson(problem);
        assertThat("json", Objects.nonNull(json));
        with(json)
                .assertThat("$.*", hasSize(5))
                .assertThat("$.type", is(type.toASCIIString()))
                .assertThat("$.title", is(title))
                .assertThat("$.status", is(Status.INSUFFICIENT_STORAGE.getStatusCode()))
                .assertThat("$.detail", is(detail))
                .assertThat("$.instance", is(instance.toASCIIString()));
    }

    @Test
    void shouldSerializeUnregisteredNonThrowableProblem() {
        final URI type = URI.create("https://my.org/problem");
        final String title = "My Custom Problem";
        final StatusType status = Status.MULTI_STATUS;
        final String detail = "My Custom Problem Encountered";
        final URI instance = URI.create("https://my.org");
        final String misc = "Custom";

        final MyProblem problem = new MyProblem(type, title, status, detail, instance, misc);
        final String json = gson.toJson(problem);
        assertThat("json", Objects.nonNull(json));
        with(json)
                .assertThat("$.*", hasSize(6))
                .assertThat("$.type", is(type.toASCIIString()))
                .assertThat("$.title", is(title))
                .assertThat("$.status", is(Status.MULTI_STATUS.getStatusCode()))
                .assertThat("$.detail", is(detail))
                .assertThat("$.instance", is(instance.toASCIIString()))
                .assertThat("$.misc", is(problem.getMisc()));
    }

    @Test
    void shouldSerializeCustomProblemWithStackTraces() {
        final int balance = 10;
        final int debit = 100;
        final Problem problem = new InsufficientFundsProblem(balance, debit);

        ProblemAdapterFactory factory = new ProblemAdapterFactory().withStacktraces();
        Gson gson = new GsonBuilder().registerTypeAdapterFactory(factory).create();

        final String json = gson.toJson(problem);
        assertThat("json", Objects.nonNull(json));
        with(json)
                .assertThat("$.*", hasSize(6))
                .assertThat("$.stacktrace", is(instanceOf(List.class)))
                .assertThat("$.stacktrace[0]", is(instanceOf(String.class)));
    }

    @Test
    void shouldSerializeProblemCause() {
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

        final String json = gson.toJson(problem);
        assertThat("json", Objects.nonNull(json));
        with(json)
                .assertThat("$.*", hasSize(4))
                .assertThat("$.cause.*", hasSize(5))
                .assertThat("$.cause.type", is("https://example.org/expired-credit-card"))
                .assertThat("$.cause.title", is("Expired Credit Card"))
                .assertThat("$.cause.status", is(400))
                .assertThat("$.cause.detail", is("Credit card is expired as of 2015-09-16T00:00:00Z"))
                .assertThat("$.cause.since", is("2015-09-16T00:00:00Z"));
    }

    @Test
    void shouldNotSerializeStacktraceByDefault() {
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

        final String json = gson.toJson(problem);
        assertThat("json", Objects.nonNull(json));
        with(json)
                .assertThat("$.*", hasSize(3))
                .assertThat("$.cause.*", hasSize(2))
                .assertNotDefined("$.stacktrace")
                .assertNotDefined("$.stackTrace"); // default name, just in case our renaming didn't apply
    }

    @Test
    void shouldSerializeStacktrace() {
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

        ProblemAdapterFactory factory = new ProblemAdapterFactory().withStacktraces();
        final Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(factory)
                .create();

        final String json = gson.toJson(problem);
        assertThat("json", Objects.nonNull(json));
        with(json)
                .assertThat("$.*", hasSize(4))
                .assertThat("$.cause.*", hasSize(3))
                .assertThat("$.stacktrace", is(instanceOf(List.class)))
                .assertThat("$.stacktrace[0]", is(instanceOf(String.class)));
    }

    @Test
    void shouldDeserializeDefaultProblem() throws IOException {
        try (final JsonReader reader = getReader("default.json")) {
            final Problem raw = gson.fromJson(reader, Problem.class);

            assertThat(raw, instanceOf(DefaultProblem.class));
            final DefaultProblem problem = (DefaultProblem) raw;

            assertThat(problem, hasFeature("type", Problem::getType, hasToString("https://example.org/not-out-of-stock")));
            assertThat(problem, hasFeature("title", Problem::getTitle, equalTo("Out of Stock")));
            assertThat(problem, hasFeature("status", Problem::getStatus, equalTo(BAD_REQUEST)));
            assertThat(problem, hasFeature("detail", Problem::getDetail, is("Item B00027Y5QG is no longer available")));
            assertThat(problem, hasFeature("parameters", DefaultProblem::getParameters, hasEntry("product", "B00027Y5QG")));
        }
    }

    @Test
    void shouldDeserializeRegisteredExceptional() throws IOException {
        try (final JsonReader reader = getReader("out-of-stock.json")) {
            final Exceptional exceptional = gson.fromJson(reader, Exceptional.class);

            assertThat(exceptional, instanceOf(OutOfStockException.class));
            final OutOfStockException problem = (OutOfStockException) exceptional;

            assertThat(problem, hasFeature("type", Problem::getType, hasToString("https://example.org/out-of-stock")));
            assertThat(problem, hasFeature("title", Problem::getTitle, equalTo("Out of Stock")));
            assertThat(problem, hasFeature("status", Problem::getStatus, equalTo(BAD_REQUEST)));
            assertThat(problem, hasFeature("detail", Problem::getDetail, is("Item B00027Y5QG is no longer available")));
        }
    }

    @Test
    void shouldDeserializeUnregisteredExceptional() throws IOException {
        try (final JsonReader reader = getReader("out-of-stock.json")) {
            final Exceptional exceptional = gson.fromJson(reader, IOProblem.class);

            assertThat(exceptional, instanceOf(IOProblem.class));
            final IOProblem problem = (IOProblem) exceptional;

            assertThat(problem, hasFeature("type", Problem::getType, hasToString("https://example.org/out-of-stock")));
            assertThat(problem, hasFeature("title", Problem::getTitle, equalTo("Out of Stock")));
            assertThat(problem, hasFeature("status", Problem::getStatus, equalTo(BAD_REQUEST)));
            assertThat(problem, hasFeature("detail", Problem::getDetail, is("Item B00027Y5QG is no longer available")));
        }
    }

    @Test
    void shouldDeserializeSpecificProblem() throws IOException {
        try (final JsonReader reader = getReader("insufficient-funds.json")) {
            final InsufficientFundsProblem problem = gson.fromJson(reader, Problem.class);

            assertThat(problem, hasFeature("balance", InsufficientFundsProblem::getBalance, equalTo(10)));
            assertThat(problem, hasFeature("debit", InsufficientFundsProblem::getDebit, equalTo(-20)));
        }
    }

    @Test
    void shouldDeserializeUnknownStatus() throws IOException {
        try (final JsonReader reader = getReader("unknown.json")) {
            final Problem problem = gson.fromJson(reader, Problem.class);

            final StatusType status = problem.getStatus();

            assertThat(status, hasFeature("status code", StatusType::getStatusCode, equalTo(666)));
            assertThat(status, hasFeature("reason phrase", StatusType::getReasonPhrase, equalTo("Unknown")));
        }
    }

    @Test
    void shouldDeserializeUntyped() throws IOException {
        try (final JsonReader reader = getReader("untyped.json")) {
            final Problem problem = gson.fromJson(reader, Problem.class);

            assertThat(problem.getType(), hasToString("about:blank"));
            assertThat(problem.getTitle(), is("Something bad"));
            assertThat(problem.getStatus(), hasFeature(StatusType::getStatusCode, is(400)));
            assertThat(problem.getDetail(), is(nullValue()));
            assertThat(problem.getInstance(), is(nullValue()));
        }
    }

    @Test
    void shouldDeserializeEmpty() throws IOException {
        final Problem problem = gson.fromJson("{}", Problem.class);

        assertThat(problem.getType(), hasToString("about:blank"));
        assertThat(problem.getTitle(), is(nullValue()));
        assertThat(problem.getStatus(), is(nullValue()));
        assertThat(problem.getDetail(), is(nullValue()));
        assertThat(problem.getInstance(), is(nullValue()));
    }

    @Test
    void shouldDeserializeCause() throws IOException {
        try (final JsonReader reader = getReader("cause.json")) {
            final ThrowableProblem problem = gson.fromJson(reader, ThrowableProblem.class);

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
    }

    @Test
    void shouldDeserializeWithProcessedStackTrace() throws IOException {
        try (final JsonReader reader = getReader("cause.json")) {
            final ThrowableProblem problem = gson.fromJson(reader, ThrowableProblem.class);

            final String stackTrace = getStackTrace(problem);
            final String[] stackTraceElements = stackTrace.split("\n");

            assertThat(stackTraceElements[1], startsWith("\tat " + ProblemBuilder.class.getName()));
        }
    }

}
