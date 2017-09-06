package org.zalando.problem;

import javax.annotation.concurrent.Immutable;

@Immutable
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
