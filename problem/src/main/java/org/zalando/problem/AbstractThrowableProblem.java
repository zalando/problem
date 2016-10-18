package org.zalando.problem;

import com.google.gag.annotation.remark.Hack;
import com.google.gag.annotation.remark.OhNoYouDidnt;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Immutable // TODO kind of a lie until we remove set(String, Object)
public abstract class AbstractThrowableProblem extends ThrowableProblem {

    private final URI type;
    private final String title;
    private final Response.StatusType status;
    private final String detail;
    private final URI instance;
    private final Map<String, Object> parameters;

    protected AbstractThrowableProblem() {
        this(null);
    }

    protected AbstractThrowableProblem(@Nullable final URI type) {
        this(type, null);
    }

    protected AbstractThrowableProblem(@Nullable final URI type,
            @Nullable final String title) {
        this(type, title, null);
    }

    protected AbstractThrowableProblem(@Nullable final URI type,
            @Nullable final String title,
            @Nullable final Response.StatusType status) {
        this(type, title, status, null);
    }

    protected AbstractThrowableProblem(@Nullable final URI type,
            @Nullable final String title,
            @Nullable final Response.StatusType status,
            @Nullable final String detail) {
        this(type, title, status, detail, null);
    }

    protected AbstractThrowableProblem(@Nullable final URI type,
            @Nullable final String title,
            @Nullable final Response.StatusType status,
            @Nullable final String detail,
            @Nullable final URI instance) {
        this(type, title, status, detail, instance, null);
    }

    protected AbstractThrowableProblem(@Nullable final URI type,
            @Nullable final String title,
            @Nullable final Response.StatusType status,
            @Nullable final String detail,
            @Nullable final URI instance,
            @Nullable final ThrowableProblem cause) {
        this(type, title, status, detail, instance, cause, null);
    }

    protected AbstractThrowableProblem(@Nullable final URI type,
            @Nullable final String title,
            @Nullable final Response.StatusType status,
            @Nullable final String detail,
            @Nullable final URI instance,
            @Nullable final ThrowableProblem cause,
            @Nullable final Map<String, Object> parameters) {
        super(cause);
        this.type = Optional.ofNullable(type).orElse(DEFAULT_TYPE);
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.instance = instance;
        this.parameters = Optional.ofNullable(parameters).orElseGet(LinkedHashMap::new);
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
    public Response.StatusType getStatus() {
        return status;
    }

    @Override
    public String getDetail() {
        return detail;
    }

    @Override
    public URI getInstance() {
        return instance;
    }

    @Override
    public Map<String, Object> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    /**
     * This is required to workaround missing support for {@link com.fasterxml.jackson.annotation.JsonAnySetter} on
     * constructors annotated with {@link com.fasterxml.jackson.annotation.JsonCreator}.
     *
     * @param key   the custom key
     * @param value the custom value
     * @see <a href="https://github.com/FasterXML/jackson-databind/issues/562">Jackson Issue 562</a>
     */
    @Hack
    @OhNoYouDidnt
    void set(final String key, final Object value) {
        parameters.put(key, value);
    }

}
