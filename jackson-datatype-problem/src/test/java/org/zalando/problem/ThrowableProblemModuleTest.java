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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public final class ThrowableProblemModuleTest {

    @Test
    public void shouldOverrideDefaultProblemMixin() {
        final ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new ProblemModule());
        mapper.registerModule(new ThrowableProblemModule());

        final Class<?> mixIn = mapper.findMixInClassFor(Problem.class);

        assertThat(mixIn, equalTo(ThrowableProblemMixIn.class));
    }

}