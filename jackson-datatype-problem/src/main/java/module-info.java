module org.zalando.problem.jackson {
    requires com.fasterxml.jackson.annotation;
    requires static org.apiguardian.api;
    requires static org.checkerframework.checker.qual;
    requires transitive tools.jackson.core;
    requires transitive tools.jackson.databind;
    requires transitive org.zalando.problem;
    exports org.zalando.problem.jackson;
    opens org.zalando.problem.jackson;
    provides tools.jackson.databind.JacksonModule
        with org.zalando.problem.jackson.ProblemModule;
}
