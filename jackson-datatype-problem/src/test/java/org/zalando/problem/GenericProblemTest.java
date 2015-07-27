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

import org.junit.Test;

import javax.ws.rs.core.Response.Status;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertThat;

public final class GenericProblemTest {

    @Test
    public void shouldCreateGenericDefaultProblem() {
        final Problem problem = GenericProblem.valueOf(Problem::create, Status.NOT_FOUND);

        assertThat(problem, instanceOf(DefaultProblem.class));
    }

    @Test
    public void shouldCreateGenericThrowableProblem() {
        final Problem problem = GenericProblem.valueOf(ThrowableProblem::create, Status.NOT_FOUND);

        assertThat(problem, instanceOf(ThrowableProblem.class));
    }

}