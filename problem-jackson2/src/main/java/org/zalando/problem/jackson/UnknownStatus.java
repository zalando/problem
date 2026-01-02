package org.zalando.problem.jackson;

import org.zalando.problem.StatusType;

final class UnknownStatus implements StatusType {

    private final int statusCode;

    UnknownStatus(final int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getReasonPhrase() {
        return "Unknown";
    }

}
