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

/**
 * An extension of the {@link Problem} interface for problems that extend {@link Exception}. Since {@link Exception}
 * is a concrete type any class can only extend one exception type. {@link ThrowableProblem} is one choice, but we
 * don't want to force people to extend from this but choose their own super class. For this they can implement this
 * interface and get the same handling as {@link ThrowableProblem} for free. A common use case would be:
 *
 * <pre>{@code
 * public final class OutOfStockException extends BusinessException implements Exceptional
 * }</pre>
 *
 * @see Exception
 * @see Problem
 * @see ThrowableProblem
 */
public interface Exceptional extends Problem {

    Exceptional getCause();

    default RuntimeException propagate() throws RuntimeException {
        throw propagateAs(RuntimeException.class);
    }

    default <X extends Throwable> X propagateAs(final Class<X> type) throws X {
        throw type.cast(this);
    }

}
