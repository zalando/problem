package org.zalando.problem;

/*
 * ⁣​
 * Jackson-datatype-Problem
 * ⁣⁣
 * Copyright (C) 2015 Zalando SE
 * ⁣⁣
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ​⁣
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.junit.Test;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.Objects;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;
import static org.junit.Assert.assertThat;
import static org.zalando.problem.MoreStatus.UNPROCESSABLE_ENTITY;

public final class ProblemMixInTest {

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new Jdk8Module())
            .registerModule(new ProblemModule());

    public ProblemMixInTest() {
        mapper.registerSubtypes(InsufficientFundsProblem.class);
    }

    @Test
    public void shouldSerializeDefaultProblem() throws JsonProcessingException {
        final Problem problem = Problem.valueOf(Status.NOT_FOUND);
        final String json = mapper.writeValueAsString(problem);

        with(json)
                .assertThat("$.*", hasSize(2))
                .assertThat("$.title", is("Not Found"))
                .assertThat("$.status", is(404));
    }

    @Test
    public void shouldSerializeCustomProperties() throws JsonProcessingException {
        final Problem problem = Problem.builder()
                .withType(URI.create("https://example.org/out-of-stock"))
                .withTitle("Out of Stock")
                .withStatus(UNPROCESSABLE_ENTITY)
                .withDetail("Item B00027Y5QG is no longer available")
                .with("product", "B00027Y5QG")
                .build();

        final String json = mapper.writeValueAsString(problem);

        with(json)
                .assertThat("$.*", hasSize(5))
                .assertThat("$.product", is("B00027Y5QG"));
    }

    @Test
    public void shouldSerializeProblemCause() throws JsonProcessingException {
        final Problem problem = Problem.builder()
                .withType(URI.create("https://example.org/preauthorization-failed"))
                .withTitle("Preauthorization Failed")
                .withStatus(UNPROCESSABLE_ENTITY)
                .withCause(Problem.builder()
                        .withType(URI.create("https://example.org/expired-credit-card"))
                        .withTitle("Expired Credit Card")
                        .withStatus(UNPROCESSABLE_ENTITY)
                        .withDetail("Credit card is expired as of 2015-09-16T00:00:00Z")
                        .with("since", "2015-09-16T00:00:00Z")
                        .build())
                .build();

        final String json = mapper.writeValueAsString(problem);

        with(json)
                .assertThat("$.cause.type", is("https://example.org/expired-credit-card"))
                .assertThat("$.cause.title", is("Expired Credit Card"))
                .assertThat("$.cause.status", is(422))
                .assertThat("$.cause.detail", is("Credit card is expired as of 2015-09-16T00:00:00Z"))
                .assertThat("$.cause.since", is("2015-09-16T00:00:00Z"));
    }

    @Test
    public void shouldNotSerializeStacktraceByDefault() throws JsonProcessingException {
        final Problem problem = Problem.builder()
                .withType(URI.create("about:blank"))
                .withTitle("Foo")
                .withStatus(Status.BAD_REQUEST)
                .withCause(Problem.builder()
                        .withType(URI.create("about:blank"))
                        .withTitle("Bar")
                        .withStatus(Status.BAD_REQUEST)
                        .build())
                .build();

        final String json = mapper.writeValueAsString(problem);

        with(json)
                .assertNotDefined("$.stacktrace");
    }

    @Test
    public void shouldSerializeStacktrace() throws JsonProcessingException {
        final Problem problem = Problem.builder()
                .withType(URI.create("about:blank"))
                .withTitle("Foo")
                .withStatus(Status.BAD_REQUEST)
                .withCause(Problem.builder()
                        .withType(URI.create("about:blank"))
                        .withTitle("Bar")
                        .withStatus(Status.BAD_REQUEST)
                        .build())
                .build();

        final ObjectMapper mapper = new ObjectMapper()
                .registerModule(new Jdk8Module())
                .registerModule(new ProblemModule().withStackTraces());

        final String json = mapper.writeValueAsString(problem);

        with(json)
                .assertThat("$.stacktrace", is(instanceOf(List.class)))
                .assertThat("$.stacktrace[0]", is(instanceOf(String.class)));
    }

    @Test
    public void shouldDeserializeDefaultProblem() throws IOException {
        final URL resource = getResource("out-of-stock.json");
        final Problem raw = mapper.readValue(resource, Problem.class);

        assertThat(raw, instanceOf(DefaultProblem.class));
        final DefaultProblem problem = (DefaultProblem) raw;

        assertThat(problem, hasFeature("type", Problem::getType, hasToString("https://example.org/out-of-stock")));
        assertThat(problem, hasFeature("title", Problem::getTitle, equalTo("Out of Stock")));
        assertThat(problem, hasFeature("status", Problem::getStatus, equalTo(UNPROCESSABLE_ENTITY)));
        assertThat(problem, hasFeature("detail", Problem::getDetail, equalTo(Optional.of("Item B00027Y5QG is no longer available"))));
        assertThat(problem, hasFeature("parameters", DefaultProblem::getParameters, hasEntry("product", "B00027Y5QG")));
    }

    @Test
    public void shouldDeserializeExceptional() throws IOException {
        final URL resource = getResource("out-of-stock.json");
        final Exceptional exceptional = mapper.readValue(resource, Exceptional.class);

        assertThat(exceptional, instanceOf(DefaultProblem.class));
        final DefaultProblem problem = (DefaultProblem) exceptional;

        assertThat(problem, hasFeature("type", Problem::getType, hasToString("https://example.org/out-of-stock")));
        assertThat(problem, hasFeature("title", Problem::getTitle, equalTo("Out of Stock")));
        assertThat(problem, hasFeature("status", Problem::getStatus, equalTo(UNPROCESSABLE_ENTITY)));
        assertThat(problem, hasFeature("detail", Problem::getDetail, equalTo(Optional.of("Item B00027Y5QG is no longer available"))));
        assertThat(problem, hasFeature("parameters", DefaultProblem::getParameters, hasEntry("product", "B00027Y5QG")));
    }

    @Test
    public void shouldDeserializeSpecificProblem() throws IOException {
        final URL resource = getResource("insufficient-funds.json");
        final Problem problem = mapper.readValue(resource, Problem.class);

        assertThat(problem, instanceOf(InsufficientFundsProblem.class));

        final InsufficientFundsProblem insufficientFundsProblem = InsufficientFundsProblem.class.cast(problem);
        assertThat(insufficientFundsProblem, hasFeature("balance", InsufficientFundsProblem::getBalance, equalTo(10)));
        assertThat(insufficientFundsProblem, hasFeature("debit", InsufficientFundsProblem::getDebit, equalTo(-20)));
    }

    @Test
    public void shouldDeserializeUnknownStatus() throws IOException {
        final URL resource = getResource("unknown.json");
        final Problem problem = mapper.readValue(resource, Problem.class);

        final StatusType status = problem.getStatus();

        assertThat(status, hasFeature("status code", StatusType::getStatusCode, equalTo(666)));
        assertThat(status, hasFeature("family", StatusType::getFamily, equalTo(Family.OTHER)));
        assertThat(status, hasFeature("reason phrase", StatusType::getReasonPhrase, equalTo("Unknown")));
    }

    @Test
    public void shouldDeserializeCause() throws IOException {
        final URL resource = getResource("cause.json");
        final ThrowableProblem problem = mapper.readValue(resource, ThrowableProblem.class);

        assertThat(problem, hasFeature("cause", Throwable::getCause, is(notNullValue())));
        final ThrowableProblem cause = problem.getCause();

        assertThat(cause, is(notNullValue()));
        assertThat(cause, instanceOf(DefaultProblem.class));

        final DefaultProblem c = (DefaultProblem) cause;

        assertThat(cause, hasFeature("type", Problem::getType, hasToString("https://example.org/expired-credit-card")));
        assertThat(cause, hasFeature("title", Problem::getTitle, equalTo("Expired Credit Card")));
        assertThat(cause, hasFeature("status", Problem::getStatus, equalTo(UNPROCESSABLE_ENTITY)));
        assertThat(cause, hasFeature("detail", Problem::getDetail, equalTo(Optional.of("Credit card is expired as of 2015-09-16T00:00:00Z"))));
        assertThat(c, hasFeature("parameters", DefaultProblem::getParameters, hasEntry("since", "2015-09-16T00:00:00Z")));
    }

    @Test
    public void shouldDeserializeWithProcessedStackTrace() throws IOException {
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
