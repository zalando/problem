package org.zalando.problem;

import com.fasterxml.jackson.annotation.JsonCreator;

import javax.annotation.concurrent.Immutable;
import javax.ws.rs.core.Response.StatusType;
import java.net.URI;
import java.util.Objects;
import java.util.Optional;

@Immutable
final class GenericProblem implements Problem {

    static final URI BASE = URI.create("http://httpstatus.es/");
    
    private final URI type;
    private final String title;
    private final StatusType status;
    private final Optional<String> detail;
    private final Optional<URI> instance;

    @JsonCreator
    GenericProblem(final URI type,
            final String title,
            final StatusType status,
            final Optional<String> detail,
            final Optional<URI> instance) {
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
    public Optional<String> getDetail() {
        return detail;
    }

    @Override
    public Optional<URI> getInstance() {
        return instance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, title, status, detail, instance);
    }

    @Override
    public boolean equals(final Object that) {
        if (this == that) {
            return true;
        } else if (that instanceof GenericProblem) {
            final GenericProblem other = (GenericProblem) that;
            return Objects.equals(type, other.type) &&
                    Objects.equals(title, other.title) &&
                    Objects.equals(status, other.status) &&
                    Objects.equals(detail, other.detail) &&
                    Objects.equals(instance, other.instance);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return type.toString(); // TODO more detail
    }

}
