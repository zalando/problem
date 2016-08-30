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

import javax.ws.rs.core.Response.Status;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;
import static org.junit.Assert.assertThat;

public final class ProblemStaticFactoryTest {

    @Test
    public void shouldCreateGenericProblem() {
        final Problem problem = Problem.valueOf(Status.NOT_FOUND);

        assertThat(problem, hasFeature("type", Problem::getType, hasToString("about:blank")));
        assertThat(problem, hasFeature("title", Problem::getTitle, equalTo("Not Found")));
        assertThat(problem, hasFeature("status", Problem::getStatus, equalTo(Status.NOT_FOUND)));
    }

    @Test
    public void shouldCreateGenericProblemWithDetail() {
        final Problem problem = Problem.valueOf(Status.NOT_FOUND, "Order 123");

        assertThat(problem, hasFeature("type", Problem::getType, hasToString("about:blank")));
        assertThat(problem, hasFeature("title", Problem::getTitle, equalTo("Not Found")));
        assertThat(problem, hasFeature("status", Problem::getStatus, equalTo(Status.NOT_FOUND)));
        assertThat(problem, hasFeature("detail", Problem::getDetail, is("Order 123")));
    }

}
