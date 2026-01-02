package org.zalando.problem.jackson3;

abstract class BusinessException extends Exception {

    BusinessException(final String message) {
        super(message);
    }

}
