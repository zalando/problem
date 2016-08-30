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

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;
import static org.junit.Assert.assertThat;

@SuppressWarnings("ConstantConditions")
public final class DefaultProblemTest {

    private final URI type = URI.create("https://example.org/out-of-stock");

    @Test
    public void shouldDefaultToAboutBlank() {
        final DefaultProblem problem = new DefaultProblem(null, null, null, null, null, null);
        assertThat(problem.getType(), hasToString("about:blank"));
    }

    @Test
    public void shouldImplementProblem() {
        final DefaultProblem problem = new DefaultProblem(type, "Out of Stock", BAD_REQUEST,
                "Item B00027Y5QG is no longer available",
                URI.create("https://example.org/e7203fd2-463b-11e5-a823-10ddb1ee7671"),
                null);

        problem.set("foo", "bar");

        assertThat(problem, hasFeature("type", Problem::getType, equalTo(type)));
        assertThat(problem, hasFeature("title", Problem::getTitle, equalTo("Out of Stock")));
        assertThat(problem, hasFeature("status", Problem::getStatus, equalTo(BAD_REQUEST)));
        assertThat(problem, hasFeature("detail", Problem::getDetail,
                is("Item B00027Y5QG is no longer available")));
        assertThat(problem, hasFeature("instance", Problem::getInstance,
                hasToString("https://example.org/e7203fd2-463b-11e5-a823-10ddb1ee7671")));
        assertThat(problem, hasFeature(DefaultProblem::getParameters, hasEntry("foo", "bar")));
    }

}