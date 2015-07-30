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

@Immutable
// not immutable to be used as a base for MixIns
class DefaultProblem extends ThrowableProblem {

    private final URI type;
    private final String title;
    private final StatusType status;
    private final Optional<String> detail;
    private final Optional<URI> instance;

    // TODO @JsonCreator in MixIn
    DefaultProblem(final URI type,
                   final String title,
                   final StatusType status,
                   final Optional<String> detail,
                   final Optional<URI> instance) {
        // TODO null checks?
        this.title = title;
        this.status = status;
        this.type = type;
        this.detail = detail;
        this.instance = instance;
    }

    @Override
    public URI getType() {
        return type;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public StatusType getStatus() {
        return status;
    }

    @Override
    public Optional<String> getDetail() {
        return detail;
    }

    @Override
    public Optional<URI> getInstance() {
        return instance;
    }

    // TODO move to ThrowableProblem?
    @Override
    public String toString() {
        return type.toString(); // TODO more detail
    }

}
