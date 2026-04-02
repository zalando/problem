module org.zalando.problem {
    requires static org.apiguardian.api;
    requires static org.checkerframework.checker.qual;
    exports org.zalando.problem;
    exports org.zalando.problem.spi;
    opens org.zalando.problem;
    uses org.zalando.problem.spi.StackTraceProcessor;
}
