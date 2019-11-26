package org.zalando.problem;

import java.net.URI;

import static org.zalando.problem.Status.BAD_REQUEST;

class OutOfStockException extends ThrowableProblem {

    private static final String TYPE_NAME = "https://example.org/out-of-stock";

    public static final URI TYPE = URI.create(TYPE_NAME);
    private String detail;

    OutOfStockException(final String detail) {
        this.detail = detail;
    }

    @Override
    public URI getType() {
        return TYPE;
    }

    @Override
    public String getTitle() {
        return "Out of Stock";
    }

    @Override
    public StatusType getStatus() {
        return BAD_REQUEST;
    }

    @Override
    public String getDetail() {
        return detail;
    }

}
