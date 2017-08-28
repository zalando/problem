package org.zalando.problem;

abstract class BusinessException extends Exception {

    BusinessException(final String message) {
        super(message);
    }

}
