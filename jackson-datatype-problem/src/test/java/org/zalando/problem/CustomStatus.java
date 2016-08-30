package org.zalando.problem;

import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

enum CustomStatus implements StatusType {

    @SuppressWarnings("unused")
    OK(200, "OK");

    private final int statusCode;
    private final Family family;
    private final String reasonPhrase;

    CustomStatus(final int statusCode, final String reasonPhrase) {
        this.statusCode = statusCode;
        this.family = Family.familyOf(statusCode);
        this.reasonPhrase = reasonPhrase;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public Family getFamily() {
        return family;
    }

    @Override
    public String getReasonPhrase() {
        return reasonPhrase;
    }

}
