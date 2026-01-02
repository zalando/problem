import org.zalando.problem.jackson3.ProblemModule;

module org.zalando.problem.jackson {
    requires com.fasterxml.jackson.annotation;
    requires static org.apiguardian.api;
    requires static org.checkerframework.checker.qual;
    requires transitive tools.jackson.core;
    requires transitive tools.jackson.databind;
    requires transitive org.zalando.problem;
    exports org.zalando.problem.jackson3;
    opens org.zalando.problem.jackson3;
    provides tools.jackson.databind.JacksonModule
        with ProblemModule;
}
