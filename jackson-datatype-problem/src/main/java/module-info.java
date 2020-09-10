module org.zalando.problem.jackson {
    requires com.fasterxml.jackson.annotation;
    requires static org.apiguardian.api;
    requires transitive com.fasterxml.jackson.core;
    requires transitive com.fasterxml.jackson.databind;
    requires transitive org.zalando.problem;
    exports org.zalando.problem.jackson;
    provides com.fasterxml.jackson.databind.Module with org.zalando.problem.jackson.ProblemModule;
}
