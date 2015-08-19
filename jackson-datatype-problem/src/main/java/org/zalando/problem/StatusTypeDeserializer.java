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

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nullable;
import javax.ws.rs.core.Response.StatusType;
import java.io.IOException;

final class StatusTypeDeserializer extends JsonDeserializer<StatusType> {

    private final ImmutableMap<Integer, StatusType> index;

    StatusTypeDeserializer(final ImmutableMap<Integer, StatusType> index) {
        this.index = index;
    }

    @Override
    public StatusType deserialize(final JsonParser json, final DeserializationContext context) throws IOException {
        final int statusCode = json.getIntValue();
        @Nullable final StatusType status = index.get(statusCode);
        return status == null ? new UnknownStatus(statusCode) : status;
    }

}
