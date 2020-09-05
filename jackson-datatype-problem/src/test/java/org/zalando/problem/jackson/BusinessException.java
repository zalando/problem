package org.zalando.problem.jackson;

abstract class BusinessException extends Exception {

    BusinessException(final String message) {
        super(message);
    }

}
