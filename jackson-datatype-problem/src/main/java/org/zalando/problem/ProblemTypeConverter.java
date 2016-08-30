package org.zalando.problem;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.net.URI;

final class ProblemTypeConverter extends StdConverter<URI, URI> {

    @Override
    public URI convert(final URI value) {
        return Problem.DEFAULT_TYPE.equals(value) ? null : value;
    }

}
