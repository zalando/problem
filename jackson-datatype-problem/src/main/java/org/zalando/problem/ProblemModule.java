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
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;

public final class ProblemModule extends Module {

    private final boolean stacktraces;
    private final Map<Integer, StatusType> statuses;

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
     * @param types status type enums
     * @throws IllegalArgumentException if there are duplicate status codes across all status types
     */
    @SafeVarargs
    public <E extends Enum & StatusType> ProblemModule(final Class<? extends E>... types)
            throws IllegalArgumentException {

        this(false, buildIndex(types));
    }

    public ProblemModule(boolean stacktraces, Map<Integer, StatusType> statuses) {
        this.stacktraces = stacktraces;
        this.statuses = Collections.unmodifiableMap(statuses);
    }


    @Override
    public String getModuleName() {
        return ProblemModule.class.getSimpleName();
    }

    @SuppressWarnings("deprecation")
    @Override
    public Version version() {
        return VersionUtil.mavenVersionFor(ProblemModule.class.getClassLoader(),
                "org.zalando", "jackson-datatype-problem");
    }

    @Override
    public void setupModule(final SetupContext context) {
        final SimpleModule module = new SimpleModule();

        module.setMixInAnnotation(Exceptional.class, stacktraces ?
                ExceptionalWithStacktraceMixin.class :
                ExceptionalMixin.class);

        module.setMixInAnnotation(DefaultProblem.class, DefaultProblemMixIn.class);
        module.setMixInAnnotation(Problem.class, ProblemMixIn.class);

        module.addSerializer(StatusType.class, new StatusTypeSerializer());
        module.addDeserializer(StatusType.class, new StatusTypeDeserializer(statuses));

        module.setupModule(context);
    }

    @SafeVarargs
    private static <E extends Enum & StatusType> Map<Integer, StatusType> buildIndex(
            final Class<? extends E>... types) {
        final Map<Integer, StatusType> index = new HashMap<>();

        for (Class<? extends E> type : types) {
            for (final E status : type.getEnumConstants()) {
                if (index.containsKey(status.getStatusCode())) {
                    throw new IllegalArgumentException("Duplicate status codes are not allowed");
                }
                index.put(status.getStatusCode(), status);
            }
        }

        return index;
    }

    public ProblemModule withStacktraces() {
        return withStacktraces(true);
    }

    public ProblemModule withStacktraces(final boolean stacktraces) {
        return new ProblemModule(stacktraces, statuses);
    }

}
