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

import org.zalando.problem.spi.StackTraceProcessor;

import java.util.Collection;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public final class JunitStackTraceProcessor implements StackTraceProcessor {

    @Override
    public Collection<StackTraceElement> process(final Collection<StackTraceElement> elements) {
        return elements.stream()
                .filter(element -> !element.getClassName().startsWith("org.junit"))
                .collect(toList());
    }

}
