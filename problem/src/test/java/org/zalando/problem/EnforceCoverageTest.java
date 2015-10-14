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

import com.google.gag.annotation.remark.Hack;
import com.google.gag.annotation.remark.OhNoYouDidnt;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.net.URI;

@Hack
@OhNoYouDidnt
public class EnforceCoverageTest {

    @Test(expected = FakeProblem.class)
    public void shouldCoverUnreachableThrowStatement() throws Exception {
        throw new FakeProblem().propagate();
    }

    static final class FakeProblem extends Exception implements Exceptional {

        @Override
        public URI getType() {
            return URI.create("about:blank");
        }

        @Override
        public String getTitle() {
            return "Fake";
        }

        @Override
        public Response.StatusType getStatus() {
            return MoreStatus.UNPROCESSABLE_ENTITY;
        }

        @Override
        public ThrowableProblem getCause() {
            return null;
        }

        @Override
        public <X extends Throwable> X propagateAs(final Class<X> type) throws X {
            return type.cast(this);
        }

    }

}
