# Problem

[![Build Status](https://img.shields.io/travis/zalando/problem.svg)](https://travis-ci.org/zalando/problem)
[![Coverage Status](https://img.shields.io/coveralls/zalando/problem.svg)](https://coveralls.io/r/zalando/problem)
[![Release](https://img.shields.io/github/release/zalando/problem.svg)](https://github.com/zalando/problem/releases)
[![Maven Central](https://img.shields.io/maven-central/v/org.zalando/problem.svg)](https://maven-badges.herokuapp.com/maven-central/org.zalando/problem)

*Problem* is a library that implements [`application/problem+json`](https://tools.ietf.org/html/draft-nottingham-http-problem-07).
It comes with an extensible set of interfaces/implementations as well as convenient functions for every day use.
It's decoupled from any JSON library, but contains a separate module for Jackson.

## Dependency

```xml
<dependency>
    <groupId>org.zalando</groupId>
    <artifactId>problem</artifactId>
    <version>${problem.version}</version>
</dependency>

<dependency>
    <groupId>org.zalando</groupId>
    <artifactId>jackson-datatype-problem</artifactId>
    <version>${problem.version}</version>
</dependency>
```

## Usage

### Generic

```java
Problem.valueOf(Status.NOT_FOUND);
```

```json
{
  "type": "http://httpstatus.es/404",
  "title": "Not Found",
  "status": 404
}
```

```java
Problem.valueOf(Status.SERVICE_UNAVAILABLE, "Database not reachable");
```

```json
{
  "type": "http://httpstatus.es/503",
  "title": "Service Unavailable",
  "status": 503,
  "detail": "Database not reachable"
}
```

### Builder

```java
Problem.builder()
    .withType(URI.create("http://example.org/out-of-stock"))
    .withTitle("Out of Stock")
    .withStatus(MoreStatus.UNPROCESSABLE_ENTITY)
    .withDetail("Item B00027Y5QG is no longer available")
    .build();
```

### Custom Problems

```java
@Immutable
public final class OutOfStockProblem implements Problem {

    private static final URI TYPE = URI.create("http://example.org/out-of-stock");

    private final Optional<String> detail;

    public OutOfStockProblem(final String detail) {
        this.detail = Optional.of(detail);
    }

    @Override
    public URI getType() {
        return TYPE;
    }

    @Override
    public String getTitle() {
        return "Out of Stock";
    }

    @Override
    public StatusType getStatus() {
        return MoreStatus.UNPROCESSABLE_ENTITY;
    }

    @Override
    public Optional<String> getDetail() {
        return detail;
    }
}
```

### Throwing Problems



## License

Copyright [2015] Zalando SE

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
