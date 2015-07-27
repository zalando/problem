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

// TODO all @Nullable or Optional<..> to be compliant with the spec?
@Immutable
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

    static Problem create(final URI type, final String title, final StatusType status) {
        return new DefaultProblem(
                type,
                title,
                status,
                Optional.empty(),
                Optional.empty()
        );
    }

    static Problem create(final URI type, final String title, final StatusType status, final String detail) {
        return new DefaultProblem(
                type,
                title,
                status,
                Optional.of(detail),
                Optional.empty()
        );
    }

    static Problem create(final URI type, final String title, final StatusType status, final String detail, final URI instance) {
        return new DefaultProblem(
                type,
                title,
                status,
                Optional.of(detail),
                Optional.of(instance)
        );
    }

}
