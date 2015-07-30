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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.ws.rs.core.Response.StatusType;
import java.net.URI;
import java.util.Optional;

abstract class DefaultProblemMixIn {

    @JsonCreator
    DefaultProblemMixIn(
            @JsonProperty("type") final URI type,
            @JsonProperty("title") final String title,
            @JsonProperty("status") final StatusType status,
            @JsonProperty("detail") final Optional<String> detail,
            @JsonProperty("instance") final Optional<URI> instance) {
        // this is just here to see whether "our" constructor matches the real one
        throw new DefaultProblem(type, title, status, detail, instance);
    }

}
