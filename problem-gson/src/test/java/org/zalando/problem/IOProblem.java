package org.zalando.problem;

import java.io.IOException;
import java.net.URI;

final class IOProblem extends IOException implements Exceptional {

    private final URI type;
    private final String title;
    private final StatusType status;
    private final String detail;
    private final URI instance;

    IOProblem(final URI type,
              final String title,
              final StatusType status,
              final String detail,
              final URI instance) {
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.instance = instance;
    }

    @Override
    public URI getType() {
        return type;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public StatusType getStatus() {
        return status;
    }

    @Override
    public String getDetail() {
        return detail;
    }

    @Override
    public URI getInstance() {
        return instance;
    }

    @Override
    public ThrowableProblem getCause() {
        return null;
    }

}