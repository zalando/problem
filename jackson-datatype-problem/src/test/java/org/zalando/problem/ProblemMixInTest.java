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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.google.common.io.Resources;
import org.junit.Test;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Optional;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;
import static org.junit.Assert.assertThat;
import static org.zalando.problem.MoreStatus.UNPROCESSABLE_ENTITY;

public final class ProblemMixInTest {

    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new Jdk8Module())
            .registerModule(new ProblemModule());

    public ProblemMixInTest() {
        mapper.registerSubtypes(InsufficientFundsProblem.class);
    }

    @Test
    public void shouldSerializeDefaultProblem() throws JsonProcessingException {
        final Problem problem = Problem.valueOf(Status.NOT_FOUND);
        final String json = mapper.writeValueAsString(problem);

        with(json)
                .assertThat("$.*", hasSize(3))
                .assertThat("$.type", hasToString("http://httpstatus.es/404"))
                .assertThat("$.title", is("Not Found"))
                .assertThat("$.status", is(404));
    }

    @Test
    public void shouldSerializeCustomProperties() throws JsonProcessingException {
        final Problem problem = Problem.builder()
                .withType(URI.create("http://example.org/out-of-stock"))
                .withTitle("Out of Stock")
                .withStatus(UNPROCESSABLE_ENTITY)
                .withDetail("Item B00027Y5QG is no longer available")
                .with("product", "B00027Y5QG")
                .build();

        final String json = mapper.writeValueAsString(problem);

        with(json)
                .assertThat("$.*", hasSize(5))
                .assertThat("$.product", is("B00027Y5QG"));
    }

    @Test
    public void shouldDeserializeDefaultProblem() throws IOException {
        final URL resource = Resources.getResource("out-of-stock.json");
        final Problem raw = mapper.readValue(resource, Problem.class);

        assertThat(raw, instanceOf(DefaultProblem.class));
        final DefaultProblem problem = (DefaultProblem) raw;

        assertThat(problem, hasFeature("type", Problem::getType, hasToString("http://example.org/out-of-stock")));
        assertThat(problem, hasFeature("title", Problem::getTitle, equalTo("Out of Stock")));
        assertThat(problem, hasFeature("status", Problem::getStatus, equalTo(UNPROCESSABLE_ENTITY)));
        assertThat(problem, hasFeature("detail", Problem::getDetail, equalTo(Optional.of("Item B00027Y5QG is no longer available"))));
        assertThat(problem, hasFeature("parameters", DefaultProblem::getParameters, hasEntry("product", "B00027Y5QG")));
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

    @Test
    public void shouldDeserializeUnknownStatus() throws IOException {
        final URL resource = Resources.getResource("unknown.json");
        final Problem problem = mapper.readValue(resource, Problem.class);

        final StatusType status = problem.getStatus();

        assertThat(status, hasFeature("status code", StatusType::getStatusCode, equalTo(666)));
        assertThat(status, hasFeature("family", StatusType::getFamily, equalTo(Family.OTHER)));
        assertThat(status, hasFeature("reason phrase", StatusType::getReasonPhrase, equalTo("Unknown")));
    }

}