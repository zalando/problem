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

import java.net.URI;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@JsonTypeName(InsufficientFundsProblem.TYPE_VALUE)
public final class InsufficientFundsProblem extends AbstractThrowableProblem {

    static final String TYPE_VALUE = "https://example.org/insufficient-funds";
    static final URI TYPE = URI.create(TYPE_VALUE);

    private final int balance;
    private final int debit;

    @JsonCreator
    public InsufficientFundsProblem(
            @JsonProperty("balance") final int balance,
            @JsonProperty("debit") final int debit) {
        super(TYPE, "Insufficient Funds", BAD_REQUEST);
        this.balance = balance;
        this.debit = debit;
    }

    public int getBalance() {
        return balance;
    }

    public int getDebit() {
        return debit;
    }

}
