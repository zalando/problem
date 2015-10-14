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

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public final class ExceptionalTest {

    @Test
    public void shouldBeAbleToThrowAndCatchThrowableProblem() {
        try {
            throw unit().propagate();
        } catch (final InsufficientFundsProblem problem) {
            assertThat(problem, hasFeature("balance", InsufficientFundsProblem::getBalance, equalTo(10)));
        } catch (final OutOfStockProblem problem) {
            fail("Should not have been out-of-stock");
        } catch (final Exception e) {
            fail("Should not have been unspecific problem");
        }
    }

    private Exceptional unit() {
        return new InsufficientFundsProblem(10, -20);
    }

}
