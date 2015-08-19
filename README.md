# Problem

[![Bubble Gum on Shoe](https://github.com/zalando/problem/raw/master/docs/bubble-gum.jpg)](https://pixabay.com/en/bubble-gum-shoes-glue-dirt-438404/)

[![Build Status](https://img.shields.io/travis/zalando/problem.svg)](https://travis-ci.org/zalando/problem)
[![Coverage Status](https://img.shields.io/coveralls/zalando/problem.svg)](https://coveralls.io/r/zalando/problem)
[![Release](https://img.shields.io/github/release/zalando/problem.svg)](https://github.com/zalando/problem/releases)
[![Maven Central](https://img.shields.io/maven-central/v/org.zalando/problem.svg)](https://maven-badges.herokuapp.com/maven-central/org.zalando/problem)

*Problem* is a library that implements [`application/problem+json`](https://tools.ietf.org/html/draft-nottingham-http-problem-07).
It comes with an extensible set of interfaces/implementations as well as convenient functions for every day use.
It's decoupled from any JSON library, but contains a separate module for Jackson.

## Note

The term problem in this document, in contrast to the [`application/problem+json`](https://tools.ietf.org/html/draft-nottingham-http-problem-07) spec **requires** the properties `type`, `title` and `status`.

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

There are different ways to express problems. Ranging from limited, but easy-to-use to highly flexible and extensible, yet with slightly more effort:

### Generic

There are cases in which an [HTTP status code](https://en.wikipedia.org/wiki/List_of_HTTP_status_codes) is basically enough to convey the necessary information. Everything you need is the status you want to repond with and we will create a problem from it:

```java
Problem.valueOf(Status.NOT_FOUND);
```

Will produce this:

```json
{
  "type": "http://httpstatus.es/404",
  "title": "Not Found",
  "status": 404
}
```

[httpstatus.es](http://httpstatus.es/) is a convenient little site that has a unique URI for every status code which includes some information about it. A perfect match for our use case.

But you may also have the need to add some little hint, e.g. as a custom title of the problem:

```java
Problem.valueOf(Status.SERVICE_UNAVAILABLE, "Database not reachable");
```

Will produce this:

```json
{
  "type": "http://httpstatus.es/503",
  "title": "Service Unavailable",
  "status": 503,
  "detail": "Database not reachable"
}
```

### Builder

Most of the time you'll need to define specific problem types, that are unique to your application. And you want to construct problems in a more flexible way. This is where the *Problem Builder* comes into play. It ofers a fluent API and allows to construct problem instances without the need to create custom classes:

```java
Problem.builder()
    .withType(URI.create("http://example.org/out-of-stock"))
    .withTitle("Out of Stock")
    .withStatus(MoreStatus.UNPROCESSABLE_ENTITY)
    .withDetail("Item B00027Y5QG is no longer available")
    .build();
```

Will produce this:

```json
{
  "type": ""http://example.org/out-of-stock",
  "title": "Out of Stock",
  "status": 422,
  "detail": "Item B00027Y5QG is no longer available"
}
```

Right now the builder does **not** allow to add custom properties, i.e. others than `type`, `title`, `status`, `detail` and `instance`. This may change in the future.

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
