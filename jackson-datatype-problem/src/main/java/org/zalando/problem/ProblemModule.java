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

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

public final class ProblemModule extends SimpleModule {

    /**
     * TODO document
     *
     * @see Status
     * @see MoreStatus
     */
    public ProblemModule() {
        this(Status.class, MoreStatus.class);
    }

    /**
     * TODO document
     *
     * @param types
     * @throws IllegalArgumentException if there are duplicate status codes across all status types
     */
    @SafeVarargs
    public <E extends Enum & StatusType> ProblemModule(final Class<? extends E>... types) throws IllegalArgumentException {
        super(ProblemModule.class.getSimpleName(), getVersion());

        setMixInAnnotation(Problem.class, ProblemMixIn.class);
        setMixInAnnotation(ThrowableProblem.class, ThrowableProblemMixin.class);
        setMixInAnnotation(DefaultProblem.class, DefaultProblemMixIn.class);

        addSerializer(StatusType.class, new StatusTypeSerializer());
        addDeserializer(StatusType.class, new StatusTypeDeserializer(buildIndex(types)));
    }

    @SuppressWarnings("deprecation")
    private static Version getVersion() {
        return VersionUtil.mavenVersionFor(ProblemModule.class.getClassLoader(), "org.zalando", "jackson-datatype-problem");
    }

    @SafeVarargs
    private final <E extends Enum & StatusType> ImmutableMap<Integer, StatusType> buildIndex(final Class<? extends E>... types) {
        final Builder<Integer, StatusType> builder = ImmutableMap.builder();

        for (final Class<? extends E> type : types) {
            for (final E status : type.getEnumConstants()) {
                builder.put(status.getStatusCode(), status);
            }
        }

        return builder.build();
    }

}
