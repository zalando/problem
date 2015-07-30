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

import javax.ws.rs.core.Response;
import java.net.URI;

import static java.util.Optional.empty;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static org.hamcrest.Matchers.hasToString;
import static org.junit.Assert.assertThat;
import static org.zalando.problem.MoreStatus.UNPROCESSABLE_ENTITY;

@SuppressWarnings("ConstantConditions")
public final class DefaultProblemTest {

    private final URI type = URI.create("http://example.org/out-of-stock");

    @Test(expected = NullPointerException.class)
    public void shouldThrowOnNullType() {
        throw new DefaultProblem(null, "Out of stock", UNPROCESSABLE_ENTITY, empty(), empty());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowOnNullTitle() {
        throw new DefaultProblem(type, null, UNPROCESSABLE_ENTITY, empty(), empty());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowOnNullStatus() {
        throw new DefaultProblem(type, "Out of Stock", null, empty(), empty());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowOnNullDetail() {
        throw new DefaultProblem(type, "Out of Stock", UNPROCESSABLE_ENTITY, null, empty());
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowOnNullInstance() {
        throw new DefaultProblem(type, "Out of Stock", UNPROCESSABLE_ENTITY, empty(), null);
    }

    /**
     *   // Returns "http://httpstatus.es/404{Order 123, instance=https://example.org/"}
     *   Problem.create(NOT_FOOUND, "Order 123", URI.create("https://example.org/").toString();
     */

    @Test
    public void shouldRenderEmptyProblem() {
        final Problem problem = Problem.create(NOT_FOUND);
        assertThat(problem, hasToString("http://httpstatus.es/404{}"));
    }

    @Test
    public void shouldRenderDetail() {
        final Problem problem = Problem.create(NOT_FOUND, "Order 123");
        assertThat(problem, hasToString("http://httpstatus.es/404{Order 123}"));
    }

    @Test
    public void shouldRenderDetailAndInstance() {
        final ThrowableProblem problem = Problem.builder(URI.create("http://httpstatus.es/404"))
                .withTitle("Not Found")
                .withStatus(NOT_FOUND)
                .withDetail("Order 123")
                .withInstance(URI.create("https://example.org/"))
                .build();
        assertThat(problem, hasToString("http://httpstatus.es/404{Order 123, instance=https://example.org/}"));
    }

}