package org.zalando.problem;

/*
 * ⁣​
 * Problem
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

import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;
import static org.junit.Assert.assertThat;
import static org.zalando.problem.MoreStatus.UNPROCESSABLE_ENTITY;

public final class ThrowableProblemTest {
    
    @Test
    public void shouldReturnThrowableProblemCause() {
        final ThrowableProblem problem = Problem.builder()
                .withType(URI.create("https://example.org/preauthorization-failed"))
                .withTitle("Preauthorization Failed")
                .withStatus(UNPROCESSABLE_ENTITY)
                .withCause(Problem.builder()
                        .withType(URI.create("https://example.org/expired-credit-card"))
                        .withTitle("Expired Credit Card")
                        .withStatus(UNPROCESSABLE_ENTITY)
                        .build())
                .build();
        
        assertThat(problem, hasFeature("cause", ThrowableProblem::getCause, notNullValue()));
    }
    
    @Test
    public void shouldReturnNullCause() {
        final ThrowableProblem problem = Problem.builder()
                .withType(URI.create("https://example.org/preauthorization-failed"))
                .withTitle("Preauthorization Failed")
                .withStatus(UNPROCESSABLE_ENTITY)
                .build();
        
        assertThat(problem, hasFeature("cause", ThrowableProblem::getCause, nullValue()));
    }

    @Test
    public void shouldReturnTitleAsMessage() {
        final ThrowableProblem problem = Problem.builder()
                .withType(URI.create("https://example.org/preauthorization-failed"))
                .withTitle("Preauthorization Failed")
                .withStatus(UNPROCESSABLE_ENTITY)
                .build();

        assertThat(problem, hasFeature("message", Throwable::getMessage, is("Preauthorization Failed")));
    }

    @Test
    public void shouldReturnTitleAndDetailAsMessage() {
        final ThrowableProblem problem = Problem.builder()
                .withType(URI.create("https://example.org/preauthorization-failed"))
                .withTitle("Preauthorization Failed")
                .withStatus(UNPROCESSABLE_ENTITY)
                .withDetail("CVC invalid")
                .build();

        assertThat(problem, hasFeature("message", Throwable::getMessage, is("Preauthorization Failed: CVC invalid")));
    }

    @Test
    public void shouldReturnCausesMessage() {
        final ThrowableProblem problem = Problem.builder()
                .withType(URI.create("https://example.org/preauthorization-failed"))
                .withTitle("Preauthorization Failed")
                .withStatus(UNPROCESSABLE_ENTITY)
                .withCause(Problem.builder()
                        .withType(URI.create("https://example.org/expired-credit-card"))
                        .withTitle("Expired Credit Card")
                        .withStatus(UNPROCESSABLE_ENTITY)
                        .build())
                .build();

        assertThat(problem, hasFeature("cause", Throwable::getCause, is(notNullValue())));
        assertThat(problem.getCause(), hasFeature("message", Throwable::getMessage, is("Expired Credit Card")));
    }

    @Test
    public void shouldPrintStackTrace() {
        final ThrowableProblem problem = Problem.builder()
                .withType(URI.create("https://example.org/preauthorization-failed"))
                .withTitle("Preauthorization Failed")
                .withStatus(UNPROCESSABLE_ENTITY)
                .withCause(Problem.builder()
                        .withType(URI.create("https://example.org/expired-credit-card"))
                        .withTitle("Expired Credit Card")
                        .withStatus(UNPROCESSABLE_ENTITY)
                        .build())
                .build();

        final String stacktrace = getStackTrace(problem);

        assertThat(stacktrace,
                startsWith("https://example.org/preauthorization-failed{422, Preauthorization Failed}"));

        assertThat(stacktrace,
                containsString("Caused by: https://example.org/expired-credit-card{422, Expired Credit Card}"));
    }

    @Test
    public void shouldProcessStackTrace() {
        final ThrowableProblem problem = Problem.builder()
                .withType(URI.create("https://example.org/preauthorization-failed"))
                .withTitle("Preauthorization Failed")
                .withStatus(UNPROCESSABLE_ENTITY)
                .withCause(Problem.builder()
                        .withType(URI.create("https://example.org/expired-credit-card"))
                        .withTitle("Expired Credit Card")
                        .withStatus(UNPROCESSABLE_ENTITY)
                        .build())
                .build();

        final String stacktrace = getStackTrace(problem);

        assertThat(stacktrace, not(containsString("org.junit")));
    }

    private String getStackTrace(final Throwable throwable) {
        final StringWriter writer = new StringWriter();
        throwable.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

}