package org.zalando.problem;

/*
 * ⁣​
 * Problem
 * ⁣⁣
 * Copyright (C) 2015 - 2016 Zalando SE
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

import com.google.gag.annotation.remark.Hack;
import com.google.gag.annotation.remark.OhNoYouDidnt;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Immutable // TODO kind of a lie until we remove set(String, Object)
public abstract class AbstractThrowableProblem extends ThrowableProblem {

    private final URI type;
    private final String title;
    private final Response.StatusType status;
    private final String detail;
    private final URI instance;
    private final Map<String, Object> parameters;

    AbstractThrowableProblem() {
        this(null);
    }

    AbstractThrowableProblem(@Nullable final URI type) {
        this(type, null);
    }

    AbstractThrowableProblem(@Nullable final URI type,
            @Nullable final String title) {
        this(type, title, null);
    }

    AbstractThrowableProblem(@Nullable final URI type,
            @Nullable final String title,
            @Nullable final Response.StatusType status) {
        this(type, title, status, null);
    }

    AbstractThrowableProblem(@Nullable final URI type,
            @Nullable final String title,
            @Nullable final Response.StatusType status,
            @Nullable final String detail) {
        this(type, title, status, detail, null);
    }

    AbstractThrowableProblem(@Nullable final URI type,
            @Nullable final String title,
            @Nullable final Response.StatusType status,
            @Nullable final String detail,
            @Nullable final URI instance) {
        this(type, title, status, detail, instance, null);
    }

    AbstractThrowableProblem(@Nullable final URI type,
            @Nullable final String title,
            @Nullable final Response.StatusType status,
            @Nullable final String detail,
            @Nullable final URI instance,
            @Nullable final ThrowableProblem cause) {
        this(type, title, status, detail, instance, cause, null);
    }

    AbstractThrowableProblem(@Nullable final URI type,
            @Nullable final String title,
            @Nullable final Response.StatusType status,
            @Nullable final String detail,
            @Nullable final URI instance,
            @Nullable final ThrowableProblem cause,
            @Nullable final Map<String, Object> parameters) {
        super(cause);
        this.type = Optional.ofNullable(type).orElse(DEFAULT_TYPE);
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.instance = instance;
        this.parameters = Optional.ofNullable(parameters).orElseGet(LinkedHashMap::new);
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
    public Response.StatusType getStatus() {
        return status;
    }

    @Override
    public String getDetail() {
        return detail;
    }

    @Override
    public URI getInstance() {
        return instance;
    }

    @Override
    public Map<String, Object> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    /**
     * This is required to workaround missing support for {@link com.fasterxml.jackson.annotation.JsonAnySetter} on
     * constructors annotated with {@link com.fasterxml.jackson.annotation.JsonCreator}.
     *
     * @param key   the custom key
     * @param value the custom value
     * @see <a href="https://github.com/FasterXML/jackson-databind/issues/562">Jackson Issue 562</a>
     */
    @Hack
    @OhNoYouDidnt
    void set(final String key, final Object value) {
        parameters.put(key, value);
    }

}
