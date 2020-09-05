package org.zalando.problem.gson;

import org.zalando.problem.StatusType;

enum CustomStatus implements StatusType {

    @SuppressWarnings("unused")
    OK(200, "OK");

    private final int statusCode;
    private final String reasonPhrase;

    CustomStatus(final int statusCode, final String reasonPhrase) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getReasonPhrase() {
        return reasonPhrase;
    }

}
