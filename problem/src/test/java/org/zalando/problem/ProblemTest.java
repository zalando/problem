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

import java.net.URI;

import static java.util.Collections.emptyMap;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;
import static org.junit.Assert.assertThat;

public final class ProblemTest {

    @Test
    public void shouldUseDefaultType() {
        final Problem problem = new EmptyProblem();

        assertThat(problem, hasFeature("type", Problem::getType, hasToString("about:blank")));
    }

    @Test
    public void shouldUseDefaultTitle() {
        final Problem problem = new EmptyProblem();

        assertThat(problem, hasFeature("title", Problem::getTitle, is(nullValue())));
    }

    @Test
    public void shouldUseDefaultStatus() {
        final Problem problem = new EmptyProblem();

        assertThat(problem, hasFeature("status", Problem::getStatus, is(nullValue())));
    }

    @Test
    public void shouldUseDefaultDetail() {
        final Problem problem = new EmptyProblem();

        assertThat(problem, hasFeature("detail", Problem::getDetail, is(nullValue())));
    }

    @Test
    public void shouldUseDefaultInstance() {
        final Problem problem = new EmptyProblem();

        assertThat(problem, hasFeature("instance", Problem::getInstance, is(nullValue())));
    }

    @Test
    public void shouldUseDefaultParameters() {
        final Problem problem = new EmptyProblem();

        assertThat(problem, hasFeature("parameters", Problem::getParameters, is(emptyMap())));
    }

    @Test
    public void simpleAbstractThrowableProblemShouldBeEmpty() {
        final Problem problem = new AbstractThrowableProblem() {
        };

        assertThat(problem, hasFeature(Problem::getType, hasToString("about:blank")));
    }

    @Test
    public void shouldRenderEmptyProblem() {
        final Problem problem = Problem.builder().build();
        assertThat(problem, hasToString("about:blank{}"));
    }

    @Test
    public void shouldRenderType() {
        final Problem problem = Problem.builder().withType(URI.create("my-problem")).build();
        assertThat(problem, hasToString("my-problem{}"));
    }

    @Test
    public void shouldRenderTitle() {
        final Problem problem = Problem.builder().withTitle("Not Found").build();
        assertThat(problem, hasToString("about:blank{Not Found}"));
    }

    @Test
    public void shouldRenderStatus() {
        final Problem problem = Problem.builder().withStatus(NOT_FOUND).build();
        assertThat(problem, hasToString("about:blank{404}"));
    }

    @Test
    public void shouldRenderDetail() {
        final Problem problem = Problem.builder().withDetail("Order 123").build();
        assertThat(problem, hasToString("about:blank{Order 123}"));
    }

    @Test
    public void shouldRenderInstance() {
        final Problem problem = Problem.valueOf(NOT_FOUND, URI.create("https://example.org/"));
        assertThat(problem, hasToString("about:blank{404, Not Found, instance=https://example.org/}"));
    }

    @Test
    public void shouldRenderFully() {
        final Problem problem = Problem.valueOf(NOT_FOUND, "Order 123", URI.create("https://example.org/"));
        assertThat(problem, hasToString("about:blank{404, Not Found, Order 123, instance=https://example.org/}"));
    }

    @Test
    public void shouldRenderCustomDetailAndInstance() {
        final ThrowableProblem problem = Problem.builder()
                .withType(URI.create("https://example.org/problem"))
                .withTitle("Not Found")
                .withStatus(NOT_FOUND)
                .withDetail("Order 123")
                .withInstance(URI.create("https://example.org/"))
                .build();

        assertThat(problem, hasToString("https://example.org/problem{404, Not Found, Order 123, instance=https://example.org/}"));
    }

    @Test
    public void shouldRenderCustomProperties() {
        final ThrowableProblem problem = Problem.builder()
                .withType(URI.create("https://example.org/problem"))
                .withTitle("Not Found")
                .withStatus(NOT_FOUND)
                .withDetail("Order 123")
                .with("foo", "bar")
                .build();

        assertThat(problem, hasToString("https://example.org/problem{404, Not Found, Order 123, foo=bar}"));
    }

}