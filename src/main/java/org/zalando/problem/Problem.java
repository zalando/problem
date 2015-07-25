package org.zalando.problem;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import javax.annotation.concurrent.Immutable;
import javax.ws.rs.core.Response.StatusType;
import java.net.URI;
import java.util.Optional;

@Immutable
@JsonTypeInfo(use = Id.NAME, 
        include = As.EXISTING_PROPERTY, 
        property = "type", 
        defaultImpl = GenericProblem.class, 
        visible = true)
public interface Problem {

    URI getType();

    String getTitle();

    StatusType getStatus();

    default Optional<String> getDetail() {
        return Optional.empty();
    }

    default Optional<URI> getInstance() {
        return Optional.empty();
    }

    static Problem valueOf(final StatusType status, final String detail) {
        return new GenericProblem(
                GenericProblem.BASE.resolve(String.valueOf(status.getStatusCode())),
                status.getReasonPhrase(),
                status,
                Optional.of(detail),
                Optional.<URI>empty()
        );
    }

}
