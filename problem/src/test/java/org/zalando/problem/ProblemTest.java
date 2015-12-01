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

import org.hamcrest.Matcher;
import org.junit.Test;

import java.net.URI;
import java.util.Optional;

import static java.util.Collections.emptyMap;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;
import static org.junit.Assert.assertThat;

public final class ProblemTest {

    @Test
    public void shouldUseDefaultDetail() {
        final Problem problem = new InsufficientFundsProblem(10, -20);

        assertThat(problem, hasFeature("detail", Problem::getDetail, isAbsent()));
    }

    @Test
    public void shouldUseDefaultInstance() {
        final Problem problem = new InsufficientFundsProblem(10, -20);

        assertThat(problem, hasFeature("instance", Problem::getInstance, isAbsent()));
    }

    @Test
    public void shouldUseDefaultParameters() {
        final Problem problem = new InsufficientFundsProblem(10, -20);

        assertThat(problem, hasFeature("parameters", Problem::getParameters, is(emptyMap())));
    }

    private <T> Matcher<Optional<T>> isAbsent() {
        return hasFeature("present", Optional::isPresent, equalTo(false));
    }

    @Test
    public void shouldRenderEmptyProblem() {
        final Problem problem = Problem.valueOf(NOT_FOUND);
        assertThat(problem, hasToString("http://httpstatus.es/404{404, Not Found}"));
    }

    @Test
    public void shouldRenderDetail() {
        final Problem problem = Problem.valueOf(NOT_FOUND, "Order 123");
        assertThat(problem, hasToString("http://httpstatus.es/404{404, Not Found, Order 123}"));
    }

    @Test
    public void shouldRenderDetailAndInstance() {
        final ThrowableProblem problem = Problem.builder()
                .withType(URI.create("http://httpstatus.es/404"))
                .withTitle("Not Found")
                .withStatus(NOT_FOUND)
                .withDetail("Order 123")
                .withInstance(URI.create("https://example.org/"))
                .build();

        assertThat(problem, hasToString("http://httpstatus.es/404{404, Not Found, Order 123, instance=https://example.org/}"));
    }

    @Test
    public void shouldRenderCustomProperties() {
        final ThrowableProblem problem = Problem.builder()
                .withType(URI.create("http://httpstatus.es/404"))
                .withTitle("Not Found")
                .withStatus(NOT_FOUND)
                .withDetail("Order 123")
                .with("foo", "bar")
                .build();

        assertThat(problem, hasToString("http://httpstatus.es/404{404, Not Found, Order 123, foo=bar}"));
    }

}