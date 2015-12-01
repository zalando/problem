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

import com.google.common.base.Joiner;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

@Immutable
public abstract class ThrowableProblem extends RuntimeException implements Problem, Exceptional {

    private static final Joiner DELIMITER = Joiner.on(": ").skipNulls();

    public ThrowableProblem() {

    }

    public ThrowableProblem(@Nullable final ThrowableProblem cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return DELIMITER.join(getTitle(), getDetail().orElse(null));
    }

    @Override
    public ThrowableProblem getCause() {
        // cast is safe, since the only way to set this is our constructor
        return (ThrowableProblem) super.getCause();
    }

    @Override
    public String toString() {
        return Problem.toString(this);
    }

    // TODO trim stack trace?!

}
