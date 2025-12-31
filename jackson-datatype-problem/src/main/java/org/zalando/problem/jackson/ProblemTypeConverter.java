package org.zalando.problem.jackson;

import tools.jackson.databind.util.StdConverter;
import org.zalando.problem.Problem;

import java.net.URI;

final class ProblemTypeConverter extends StdConverter<URI, URI> {

    @Override
    public URI convert(final URI value) {
        return Problem.DEFAULT_TYPE.equals(value) ? null : value;
    }

}
