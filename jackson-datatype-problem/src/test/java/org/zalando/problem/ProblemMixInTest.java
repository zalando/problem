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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import org.junit.Test;

import javax.ws.rs.core.Response.Status;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.Matchers.*;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;
import static org.junit.Assert.assertThat;

public final class ProblemMixInTest {

    private final ObjectMapper mapper = new ObjectMapper()
            .enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY) // for deterministic tests
            .findAndRegisterModules();

    public ProblemMixInTest() {
        mapper.registerSubtypes(InsufficientFundsProblem.class);
    }

    @Test
    public void shouldSerializeDefaultProblem() throws JsonProcessingException {
        final Problem problem = Problem.create(Status.NOT_FOUND);
        final String json = mapper.writeValueAsString(problem);

        with(json)
                .assertThat("$.*", hasSize(3))
                .assertThat("$.type", hasToString("http://httpstatus.es/404"))
                .assertThat("$.title", equalTo("Not Found"))
                .assertThat("$.status", equalTo(404));
    }

    @Test
    public void shouldDeserializeDefaultProblem() throws IOException {
        final URL resource = Resources.getResource("out-of-stock.json");
        final Problem problem = mapper.readValue(resource, Problem.class);

        assertThat(problem, hasFeature("type", Problem::getType, hasToString("http://example.org/out-of-stock")));
        assertThat(problem, hasFeature("title", Problem::getTitle, equalTo("Out of Stock")));
        assertThat(problem, hasFeature("status", Problem::getStatus, equalTo(MoreStatus.UNPROCESSABLE_ENTITY)));
        assertThat(problem, hasFeature("detail", Problem::getDetail, equalTo(Optional.of("Product ABC"))));
    }

    @Test
    public void shouldDeserializeSpecificProblem() throws IOException {
        final URL resource = Resources.getResource("insufficient-funds.json");
        final Problem problem = mapper.readValue(resource, Problem.class);

        assertThat(problem, instanceOf(InsufficientFundsProblem.class));

        final InsufficientFundsProblem insufficientFundsProblem = InsufficientFundsProblem.class.cast(problem);
        assertThat(insufficientFundsProblem, hasFeature("balance", InsufficientFundsProblem::getBalance, equalTo(10)));
        assertThat(insufficientFundsProblem, hasFeature("debit", InsufficientFundsProblem::getDebit, equalTo(-20)));
    }

}