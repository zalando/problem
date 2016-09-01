package org.zalando.problem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URI;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public final class IOProblem extends IOException implements Exceptional {

    private final URI type;
    private final String title;
    private final Response.StatusType status;
    private final String detail;
    private final URI instance;

    @JsonCreator
    public IOProblem(@JsonProperty("type") final URI type,
            @JsonProperty("title") final String title,
            @JsonProperty("status") final Response.StatusType status,
            @JsonProperty("detail") final String detail,
            @JsonProperty("instance") final URI instance) {
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
    public Response.StatusType getStatus() {
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