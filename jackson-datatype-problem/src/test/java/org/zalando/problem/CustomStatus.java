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

import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

enum CustomStatus implements StatusType {

    OK(200, "OK");

    private final int statusCode;
    private final Family family;
    private final String reasonPhrase;

    private CustomStatus(int statusCode, String reasonPhrase) {
        this.statusCode = statusCode;
        this.family = Family.familyOf(statusCode);
        this.reasonPhrase = reasonPhrase;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public Family getFamily() {
        return family;
    }

    @Override
    public String getReasonPhrase() {
        return reasonPhrase;
    }

}
