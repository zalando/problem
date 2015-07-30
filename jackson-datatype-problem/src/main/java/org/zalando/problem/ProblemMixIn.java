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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.ws.rs.core.Response.StatusType;
import java.net.URI;
import java.util.Optional;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_ABSENT;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        defaultImpl = DefaultProblem.class,
        visible = true)
interface ProblemMixIn extends Problem {

    @JsonProperty("type")
    @Override
    URI getType();

    @JsonProperty("title")
    @Override
    String getTitle();

    @JsonProperty("status")
    @Override
    StatusType getStatus();

    @JsonProperty("detail")
    @JsonInclude(NON_ABSENT)
    @Override
    Optional<String> getDetail();

    @JsonProperty("instance")
    @JsonInclude(NON_ABSENT)
    @Override
    Optional<URI> getInstance();

}
