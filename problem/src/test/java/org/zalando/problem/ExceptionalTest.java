package org.zalando.problem;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hobsoft.hamcrest.compose.ComposeMatchers.hasFeature;
import static org.junit.jupiter.api.Assertions.fail;

final class ExceptionalTest {

    @Test
    void shouldBeAbleToThrowAndCatchThrowableProblem() {
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
