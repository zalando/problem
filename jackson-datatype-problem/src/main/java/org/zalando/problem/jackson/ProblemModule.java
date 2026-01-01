package org.zalando.problem.jackson;

import tools.jackson.core.Version;
import tools.jackson.core.util.VersionUtil;
import tools.jackson.databind.module.SimpleModule;
import org.apiguardian.api.API;
import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Exceptional;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.StatusType;
import tools.jackson.databind.JacksonModule;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.apiguardian.api.API.Status.STABLE;

@API(status = STABLE)
public final class ProblemModule extends JacksonModule {

    private final boolean stackTraces;
    private final Map<Integer, StatusType> statuses;

    /**
     * TODO document
     *
     * @see Status
     */
    public ProblemModule() {
        this(Status.class);
    }

    /**
     * TODO document
     *
     * @param <E>   generic enum type
     * @param types status type enums
     * @throws IllegalArgumentException if there are duplicate status codes across all status types
     */
    @SafeVarargs
    public <E extends Enum<?> & StatusType> ProblemModule(final Class<? extends E>... types)
            throws IllegalArgumentException {

        this(false, buildIndex(types));
    }

    private ProblemModule(final boolean stackTraces, final Map<Integer, StatusType> statuses) {
        this.stackTraces = stackTraces;
        this.statuses = statuses;
    }


    @Override
    public String getModuleName() {
        return ProblemModule.class.getSimpleName();
    }

    @Override
    public Version version() {
        Properties p = loadProps();
        String version = p.getProperty("module.version");
        String groupId = p.getProperty("module.groupId");
        String artifactId = p.getProperty("module.name");

        return (version != null)
                ? VersionUtil.parseVersion(version, groupId, artifactId)
                : Version.unknownVersion();
    }

    @Override
    public void setupModule(final SetupContext context) {
        final SimpleModule module = new SimpleModule();

        module.setMixInAnnotation(Exceptional.class, stackTraces ?
                ExceptionalMixin.class :
                ExceptionalWithoutStacktraceMixin.class);

        module.setMixInAnnotation(DefaultProblem.class, AbstractThrowableProblemMixIn.class);
        module.setMixInAnnotation(Problem.class, ProblemMixIn.class);

        module.addSerializer(StatusType.class, new StatusTypeSerializer());
        module.addDeserializer(StatusType.class, new StatusTypeDeserializer(statuses));

        module.setupModule(context);
    }

    @SafeVarargs
    private static <E extends Enum<?> & StatusType> Map<Integer, StatusType> buildIndex(
            final Class<? extends E>... types) {
        final Map<Integer, StatusType> index = new HashMap<>();

        for (final Class<? extends E> type : types) {
            for (final E status : type.getEnumConstants()) {
                if (index.containsKey(status.getStatusCode())) {
                    throw new IllegalArgumentException("Duplicate status codes are not allowed");
                }
                index.put(status.getStatusCode(), status);
            }
        }

        return Collections.unmodifiableMap(index);
    }

    public ProblemModule withStackTraces() {
        return withStackTraces(true);
    }

    public ProblemModule withStackTraces(final boolean stackTraces) {
        return new ProblemModule(stackTraces, statuses);
    }

    private static final String VERSION_RESOURCE =
            "/META-INF/org.zalando.problem.jackson/problem-module.properties";

    private static Properties loadProps() {
        Properties props = new Properties();
        try (InputStream in =
                     ProblemModule.class.getResourceAsStream(VERSION_RESOURCE)) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException ignored) {
        }
        return props;
    }

}
