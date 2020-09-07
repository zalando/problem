package org.zalando.problem.gson;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

import org.zalando.problem.AbstractThrowableProblem;

import static org.zalando.problem.Status.BAD_REQUEST;

final class InsufficientFundsProblem extends AbstractThrowableProblem {

    public static final String TYPE_VALUE = "https://example.org/insufficient-funds";
    public static final URI TYPE = URI.create(TYPE_VALUE);

    private final int balance;
    private final int debit;

    InsufficientFundsProblem(
            final int balance,
            final int debit) {
        this(balance, debit, Collections.emptyMap());
    }

    InsufficientFundsProblem(
            final int balance,
            final int debit,
            final Map<String, Object> parameters) {
        super(TYPE, "Insufficient Funds", BAD_REQUEST, null, null, null, parameters);
        this.balance = balance;
        this.debit = debit;
    }

    public int getBalance() {
        return balance;
    }

    public int getDebit() {
        return debit;
    }

}
