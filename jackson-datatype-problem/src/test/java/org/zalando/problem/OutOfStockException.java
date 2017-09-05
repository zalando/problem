package org.zalando.problem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.ws.rs.core.Response;
import java.net.URI;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@JsonTypeName(OutOfStockException.TYPE_NAME)
public class OutOfStockException extends BusinessException implements Exceptional {

    static final String TYPE_NAME = "https://example.org/out-of-stock";
    private static final URI TYPE = URI.create(TYPE_NAME);

    @JsonCreator
    public OutOfStockException(@JsonProperty("detail") final String detail) {
        super(detail);
    }

    @Override
    public URI getType() {
        return TYPE;
    }

    @Override
    public String getTitle() {
        return "Out of Stock";
    }

    @Override
    public Response.StatusType getStatus() {
        return BAD_REQUEST;
    }

    @Override
    public String getDetail() {
        return getMessage();
    }

    @Override
    public ThrowableProblem getCause() {
        return null;
    }

}
