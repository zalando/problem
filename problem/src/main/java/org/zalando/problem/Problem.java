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

import javax.annotation.concurrent.Immutable;
import javax.ws.rs.core.Response.StatusType;
import java.net.URI;
import java.util.Optional;

/**
 * @see <a href="https://tools.ietf.org/html/draft-nottingham-http-problem-07">Problem Details for HTTP APIs</a>
 */
@Immutable
// TODO document that type, title and status are *NOT* optional, in contrast to the spec
public interface Problem {

    URI getType();

    String getTitle();

    StatusType getStatus();

    default Optional<String> getDetail() {
        return Optional.empty();
    }

    default Optional<URI> getInstance() {
        return Optional.empty();
    }

    static ProblemBuilder builder() {
        return new ProblemBuilder();
    }

    // TODO javadoc
    static Problem create(final StatusType status) {
        return GenericProblems.create(status).build();
    }

    // TODO javadoc
    static Problem create(final StatusType status, final String detail) {
        return GenericProblems.create(status).withDetail(detail).build();
    }

}
