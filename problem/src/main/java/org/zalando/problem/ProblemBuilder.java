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
import java.util.Optional;

// TODO does this class really carry its own weight?
public final class ProblemBuilder {

    private URI type;
    private String title;
    private StatusType status;

    private Optional<String> detail;
    private Optional<URI> instance;

    public ProblemBuilder(final URI type) {
        this.type = type;
    }

    public ProblemBuilder withTitle(@Nullable String title) {
        this.title = title;
        return this;
    }

    public ProblemBuilder withStatus(@Nullable StatusType status) {
        this.status = status;
        return this;
    }

    public ProblemBuilder withDetail(@Nullable String detail) {
        this.detail = Optional.ofNullable(detail);
        return this;
    }

    public ProblemBuilder withInstance(@Nullable URI instance) {
        this.instance = Optional.ofNullable(instance);
        return this;
    }

    public ThrowableProblem build() {
        // TODO fail on null
        return new DefaultProblem(type, title, status, detail, instance);
    }

}
