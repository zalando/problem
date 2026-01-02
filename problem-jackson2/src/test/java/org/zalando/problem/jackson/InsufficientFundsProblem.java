package org.zalando.problem.jackson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.zalando.problem.AbstractThrowableProblem;

import java.net.URI;

import static org.zalando.problem.Status.BAD_REQUEST;

@JsonTypeName(InsufficientFundsProblem.TYPE_VALUE)
public final class InsufficientFundsProblem extends AbstractThrowableProblem {

    static final String TYPE_VALUE = "https://example.org/insufficient-funds";
    private static final URI TYPE = URI.create(TYPE_VALUE);

    private final int balance;
    private final int debit;

    @JsonCreator
    public InsufficientFundsProblem(
            @JsonProperty("balance") final int balance,
            @JsonProperty("debit") final int debit) {
        super(TYPE, "Insufficient Funds", BAD_REQUEST);
        this.balance = balance;
        this.debit = debit;
    }

    int getBalance() {
        return balance;
    }

    int getDebit() {
        return debit;
    }

}
