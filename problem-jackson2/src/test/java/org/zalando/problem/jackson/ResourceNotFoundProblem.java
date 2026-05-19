package org.zalando.problem.jackson;

import java.net.URI;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public final class ResourceNotFoundProblem extends AbstractThrowableProblem {

    private static final URI TYPE = URI.create(
            "https://your-company.com/problems/ResourceNotFoundProblem.html"
    );

    private final String id;

    @JsonCreator
    public ResourceNotFoundProblem(
            @JsonProperty("id") final String id,
            @JsonProperty("detail") final String detail
    ) {
        super(TYPE, "Resource not found", Status.NOT_FOUND, detail);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
