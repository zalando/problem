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

import javax.annotation.Nullable;
import javax.ws.rs.core.Response.StatusType;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class ProblemBuilder {

    private static final Set<String> RESERVED_PROPERTIES = new HashSet<>(Arrays.asList(
            "type", "title", "status", "detail", "instance", "cause"
    ));

    private URI type;
    private String title;
    private StatusType status;

    private Optional<String> detail = Optional.empty();
    private Optional<URI> instance = Optional.empty();
    @Nullable private ThrowableProblem cause;
    private final Map<String, Object> parameters = new LinkedHashMap<>();

    /**
     * @see Problem#builder()
     */
    ProblemBuilder() {

    }

    public ProblemBuilder withType(final URI type) {
        this.type = type;
        return this;
    }

    public ProblemBuilder withTitle(@Nullable final String title) {
        this.title = title;
        return this;
    }

    public ProblemBuilder withStatus(@Nullable final StatusType status) {
        this.status = status;
        return this;
    }

    public ProblemBuilder withDetail(@Nullable final String detail) {
        this.detail = Optional.ofNullable(detail);
        return this;
    }

    public ProblemBuilder withInstance(@Nullable final URI instance) {
        this.instance = Optional.ofNullable(instance);
        return this;
    }

    public ProblemBuilder withCause(@Nullable final ThrowableProblem cause) {
        this.cause = cause;
        return this;
    }

    /**
     *
     * @param key
     * @param value
     * @return
     * @throws IllegalArgumentException if key is any of type, title, status, detail or instance
     */
    public ProblemBuilder with(final String key, final Object value) throws IllegalArgumentException {
        if (RESERVED_PROPERTIES.contains(key)) {
            throw new IllegalArgumentException("Property " + key + " is reserved");
        }
        parameters.put(key, value);
        return this;
    }

    public ThrowableProblem build() {
        final DefaultProblem problem = new DefaultProblem(type, title, status, detail, instance, cause);
        parameters.forEach(problem::set);
        return problem;
    }

}
