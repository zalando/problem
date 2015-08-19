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

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
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

    private <T> Matcher<Optional<T>> isAbsent() {
        return hasFeature("present", Optional::isPresent, equalTo(false));
    }

}