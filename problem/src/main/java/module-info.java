module org.zalando.problem.gson {
    requires static org.apiguardian.api;
    requires transitive com.google.gson;
    requires transitive org.zalando.problem;
    exports org.zalando.problem.gson;
}