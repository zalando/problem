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

import javax.ws.rs.core.Response.StatusType;
import java.net.URI;

/**
 * @see <a href="http://httpstatus.es/">http://httpstatus.es/</a>
 */
public final class GenericProblem {

    private static final URI BASE = URI.create("http://httpstatus.es/");

    @FunctionalInterface
    public interface Creator {
        Problem create(final URI type, final String title, final StatusType status);
    }

    static Problem valueOf(final Creator creator, final StatusType status) {
        return creator.create(
                resolve(status),
                status.getReasonPhrase(),
                status
        );
    }

    @FunctionalInterface
    public interface DetailCreator {
        Problem create(final URI type, final String title, final StatusType status, final String detail);
    }

    static Problem valueOf(final DetailCreator creator, final StatusType status, final String detail) {
        return creator.create(
                resolve(status),
                status.getReasonPhrase(),
                status,
                detail
        );
    }

    @FunctionalInterface
    public interface InstanceCreator {
        Problem create(final URI type, final String title, final StatusType status, final String detail, final URI instance);
    }

    static Problem valueOf(final InstanceCreator creator, final StatusType status, final String detail, final URI instance) {
        return creator.create(
                resolve(status),
                status.getReasonPhrase(),
                status,
                detail,
                instance
        );
    }

    private static URI resolve(StatusType status) {
        return BASE.resolve(String.valueOf(status.getStatusCode()));
    }

}
