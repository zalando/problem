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

import static java.util.Optional.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasToString;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;
import static org.junit.Assert.assertThat;
import static org.zalando.problem.MoreStatus.UNPROCESSABLE_ENTITY;

@SuppressWarnings("ConstantConditions")
public final class DefaultProblemTest {

    private final URI type = URI.create("https://example.org/out-of-stock");

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

    @Test
    public void shouldImplementProblem() {
        final Problem problem = new DefaultProblem(type, "Out of Stock", UNPROCESSABLE_ENTITY,
                Optional.of("Item B00027Y5QG is no longer available"),
                Optional.of(URI.create("https://example.org/e7203fd2-463b-11e5-a823-10ddb1ee7671")));

        assertThat(problem, hasFeature("type", Problem::getType, equalTo(type)));
        assertThat(problem, hasFeature("title", Problem::getTitle, equalTo("Out of Stock")));
        assertThat(problem, hasFeature("status", Problem::getStatus, equalTo(UNPROCESSABLE_ENTITY)));
        assertThat(problem, hasFeature("detail", Problem::getDetail, isPresent()));
        assertThat(problem, hasFeature("detail", Problem::getDetail,
                hasValue(equalTo("Item B00027Y5QG is no longer available"))));
        assertThat(problem, hasFeature("instance", Problem::getInstance, isPresent()));
        assertThat(problem, hasFeature("instance", Problem::getInstance,
                hasValue(hasToString("https://example.org/e7203fd2-463b-11e5-a823-10ddb1ee7671"))));
    }

    private <T> Matcher<Optional<T>> isPresent() {
        return hasFeature("present", Optional::isPresent, equalTo(true));
    }

    private <T> Matcher<Optional<T>> hasValue(final Matcher<? super T> matcher) {
        return hasFeature("value", Optional::get, matcher);
    }

}