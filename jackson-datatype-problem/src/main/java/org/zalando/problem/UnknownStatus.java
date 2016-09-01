package org.zalando.problem;

import javax.annotation.concurrent.Immutable;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;

@Immutable
final class UnknownStatus implements Response.StatusType {

    private final int statusCode;

    UnknownStatus(final int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public Family getFamily() {
        return Family.OTHER;
    }

    @Override
    public String getReasonPhrase() {
        return "Unknown";
    }

}
