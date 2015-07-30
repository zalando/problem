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

import javax.ws.rs.core.Response.StatusType;
import java.net.URI;
import java.util.Optional;

public final class OutOfStockProblem extends ThrowableProblem {

    static final String TYPE_VALUE = "http://example.org/out-of-stock";
    static final URI TYPE = URI.create(TYPE_VALUE);

    private final Optional<String> detail;

    public OutOfStockProblem(final String detail) {
        this.detail = Optional.of(detail);
    }

    @Override
    public URI getType() {
        return TYPE;
    }

    @Override
    public String getTitle() {
        return "Insufficient Funds";
    }

    @Override
    public StatusType getStatus() {
        return MoreStatus.UNPROCESSABLE_ENTITY;
    }

    @Override
    public Optional<String> getDetail() {
        return detail;
    }

}
