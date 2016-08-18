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
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.ws.rs.core.Response;
import java.net.URI;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@JsonTypeName(OutOfStockException.TYPE_NAME)
public class OutOfStockException extends BusinessException implements Exceptional {

    public static final String TYPE_NAME = "https://example.org/out-of-stock";
    public static final URI TYPE = URI.create(TYPE_NAME);

    @JsonCreator
    public OutOfStockException(@JsonProperty("detail") final String detail) {
        super(detail);
    }

    @Override
    public URI getType() {
        return TYPE;
    }

    @Override
    public String getTitle() {
        return "Out of Stock";
    }

    @Override
    public Response.StatusType getStatus() {
        return BAD_REQUEST;
    }

    @Override
    public String getDetail() {
        return getMessage();
    }

    @Override
    public ThrowableProblem getCause() {
        return null;
    }

}
