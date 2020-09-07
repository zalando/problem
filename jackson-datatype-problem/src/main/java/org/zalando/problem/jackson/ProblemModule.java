package org.zalando.problem.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apiguardian.api.API;

import org.zalando.problem.DefaultProblem;
import org.zalando.problem.Exceptional;
import org.zalando.problem.Problem;
import org.zalando.problem.Status;
import org.zalando.problem.StatusType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.apiguardian.api.API.Status.STABLE;

@API(status = STABLE)
public final class ProblemModule extends Module {

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

    @SuppressWarnings("deprecation")
    @Override
    public Version version() {
        return VersionUtil.mavenVersionFor(ProblemModule.class.getClassLoader(),
                "org.zalando", "jackson-datatype-problem");
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

}
