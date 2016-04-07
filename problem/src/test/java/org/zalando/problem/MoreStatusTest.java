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
import javax.ws.rs.core.Response.StatusType;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;
import static org.junit.Assert.assertThat;

public final class MoreStatusTest {

    @Test
    public void shouldBeDistinctFromStatus() {
        Stream.of(MoreStatus.values()).map(StatusType::getStatusCode).forEach(code -> {
            final Status status = Status.fromStatusCode(code);
            assertThat("Duplicate code: " + code, status, is(nullValue()));
        });
    }

    @Test
    public void shouldHaveFamily() {
        Stream.of(MoreStatus.values()).forEach(status ->
                assertThat(status, hasFeature("family", StatusType::getFamily, notNullValue())));
    }

    @Test
    public void shouldHaveReasonPhrase() {
        Stream.of(MoreStatus.values()).forEach(status ->
                assertThat(status, hasFeature("reason phrase", StatusType::getReasonPhrase, not(isEmptyOrNullString()))));
    }

}