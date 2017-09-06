package org.zalando.problem;

final class GenericProblems {

    GenericProblems() throws Exception {
        throw new IllegalAccessException();
    }

    static ProblemBuilder create(final StatusType status) {
        return Problem.builder()
                .withTitle(status.getReasonPhrase())
                .withStatus(status);
    }

}
