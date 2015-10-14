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

import javax.ws.rs.core.Response;
import java.net.URI;

public class OutOfStockException extends BusinessException implements Exceptional {

    @Override
    public URI getType() {
        return URI.create("about:blank");
    }

    @Override
    public String getTitle() {
        return "Out of Stock";
    }

    @Override
    public Response.StatusType getStatus() {
        return MoreStatus.UNPROCESSABLE_ENTITY;
    }

    @Override
    public ThrowableProblem getCause() {
        return null;
    }

}
