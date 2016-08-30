package org.zalando.problem;

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
