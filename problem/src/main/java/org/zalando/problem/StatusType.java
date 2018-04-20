package org.zalando.problem;


import org.apiguardian.api.API;

import static org.apiguardian.api.API.Status.STABLE;

/**
 * Base interface for statuses.
 */
@API(status = STABLE)
public interface StatusType {

    /**
     * Get the associated status code.
     *
     * @return the status code.
     */
    int getStatusCode();

    /**
     * Get the reason phrase.
     *
     * @return the reason phrase.
     */
    String getReasonPhrase();

}
