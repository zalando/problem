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

import org.zalando.problem.spi.StackTraceProcessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public final class JacksonStackTraceProcessor implements StackTraceProcessor {

    @Override
    public Collection<StackTraceElement> process(final Collection<StackTraceElement> collection) {
        final ArrayList<StackTraceElement> elements = new ArrayList<>(collection);

        return elements.stream()
                .filter(startsWith("sun.reflect", "java.lang.reflect", "com.fasterxml.jackson").negate())
                .findFirst()
                .map(elements::indexOf)
                .map(subList(elements))
                .orElse(elements);
    }

    private Predicate<StackTraceElement> startsWith(final String... prefixes) {
        return element ->
                Arrays.stream(prefixes).anyMatch(prefix ->
                        element.getClassName().startsWith(prefix));
    }

    private <T> Function<Integer, List<T>> subList(final List<T> list) {
        return index -> list.subList(index, list.size());
    }

}
