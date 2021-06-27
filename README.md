# Problem

[![Bubble Gum on Shoe](docs/bubble-gum.jpg)](https://pixabay.com/en/bubble-gum-shoes-glue-dirt-438404/)

[![Stability: Sustained](https://masterminds.github.io/stability/sustained.svg)](https://masterminds.github.io/stability/sustained.html)
![Build Status](https://github.com/zalando/problem/workflows/build/badge.svg)
[![Coverage Status](https://img.shields.io/coveralls/zalando/problem/main.svg)](https://coveralls.io/r/zalando/problem)
[![Code Quality](https://img.shields.io/codacy/grade/fff557c57a9345d1afba5cc234cfbbd7/main.svg)](https://www.codacy.com/app/whiskeysierra/problem)
[![Javadoc](http://javadoc.io/badge/org.zalando/problem.svg)](http://www.javadoc.io/doc/org.zalando/problem)
[![Release](https://img.shields.io/github/release/zalando/problem.svg)](https://github.com/zalando/problem/releases)
[![Maven Central](https://img.shields.io/maven-central/v/org.zalando/problem.svg)](https://maven-badges.herokuapp.com/maven-central/org.zalando/problem)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://raw.githubusercontent.com/zalando/problem/main/LICENSE)

> **Problem** noun, /ˈpɹɒbləm/: A difficulty that has to be resolved or dealt with.

*Problem* is a library that implements 
[`application/problem+json`](https://tools.ietf.org/html/rfc7807).
It comes with an extensible set of interfaces/implementations as well as convenient functions for every day use.
It's decoupled from any JSON library, but contains a separate module for Jackson.

## Features

- proposes a common approach for expressing errors in REST API implementations
- compatible with `application/problem+json`

## Dependencies

- Java 8
- Any build tool using Maven Central, or direct download
- Jackson (optional)
- Gson (optional)

## Installation

Add the following dependency to your project:

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
<dependency>
    <groupId>org.zalando</groupId>
    <artifactId>problem-gson</artifactId>
    <version>${problem.version}</version>
</dependency>
```

### Java Modules

Even though the minimum requirement is still Java 8, all modules are Java 9 compatible:

```java
module org.example {
    requires org.zalando.problem;
    // pick needed dependencies
    requires org.zalando.problem.jackson;
    requires org.zalando.problem.gson;
}
```

## Configuration

In case you're using Jackson, make sure you register the module with your `ObjectMapper`:

```java
ObjectMapper mapper = new ObjectMapper()
    .registerModule(new ProblemModule());
```

Alternatively, you can use the SPI capabilities:

```java
ObjectMapper mapper = new ObjectMapper()
    .findAndRegisterModules();
```

## Usage

### Creating problems

There are different ways to express problems. Ranging from limited, but easy-to-use to highly flexible and extensible, 
yet with slightly more effort:

#### Generic

There are cases in which an [HTTP status code](https://en.wikipedia.org/wiki/List_of_HTTP_status_codes) is basically 
enough to convey the necessary information. Everything you need is the status you want to respond with and we will 
create a problem from it:

```java
Problem.valueOf(Status.NOT_FOUND);
```

Will produce this:

```json
{
  "title": "Not Found",
  "status": 404
}
```

As specified by [Predefined Problem Types](https://tools.ietf.org/html/rfc7807#section-4.2):

> The "about:blank" URI, when used as a problem type,
> indicates that the problem has no additional semantics beyond that of
> the HTTP status code.
  
> When "about:blank" is used, the title SHOULD be the same as the
> recommended HTTP status phrase for that code (e.g., "Not Found" for
> 404, and so on), although it MAY be localized to suit client
> preferences (expressed with the Accept-Language request header).

But you may also have the need to add some little hint, e.g. as a custom detail of the problem:

```java
Problem.valueOf(Status.SERVICE_UNAVAILABLE, "Database not reachable");
```

Will produce this:

```json
{
  "title": "Service Unavailable",
  "status": 503,
  "detail": "Database not reachable"
}
```

#### Builder

Most of the time you'll need to define specific problem types, that are unique to your application. And you want to 
construct problems in a more flexible way. This is where the *Problem Builder* comes into play. It offers a fluent API 
and allows to construct problem instances without the need to create custom classes:

```java
Problem.builder()
    .withType(URI.create("https://example.org/out-of-stock"))
    .withTitle("Out of Stock")
    .withStatus(BAD_REQUEST)
    .withDetail("Item B00027Y5QG is no longer available")
    .build();
```

Will produce this:

```json
{
  "type": "https://example.org/out-of-stock",
  "title": "Out of Stock",
  "status": 400,
  "detail": "Item B00027Y5QG is no longer available"
}
```

Alternatively you can add custom properties, i.e. others than `type`, `title`, `status`, `detail` and `instance`:

```java
Problem.builder()
    .withType(URI.create("https://example.org/out-of-stock"))
    .withTitle("Out of Stock")
    .withStatus(BAD_REQUEST)
    .withDetail("Item B00027Y5QG is no longer available")
    .with("product", "B00027Y5QG")
    .build();
```

Will produce this:

```json
{
  "type": "https://example.org/out-of-stock",
  "title": "Out of Stock",
  "status": 400,
  "detail": "Item B00027Y5QG is no longer available",
  "product": "B00027Y5QG"
}
```

#### Custom Problems

The highest degree of flexibility and customizability is achieved by implementing `Problem` directly. This is 
especially convenient if you refer to it in a lot of places, i.e. it makes it easier to share. Alternatively you can
extend `AbstractThrowableProblem`:

```java
@Immutable
public final class OutOfStockProblem extends AbstractThrowableProblem {

    static final URI TYPE = URI.create("https://example.org/out-of-stock");

    private final String product;

    public OutOfStockProblem(final String product) {
        super(TYPE, "Out of Stock", BAD_REQUEST, format("Item %s is no longer available", product));
        this.product = product;
    }

    public String getProduct() {
        return product;
    }

}
```

```java
new OutOfStockProblem("B00027Y5QG");
```

Will produce this:

```json
{
  "type": "https://example.org/out-of-stock",
  "title": "Out of Stock",
  "status": 400,
  "detail": "Item B00027Y5QG is no longer available",
  "product": "B00027Y5QG"
}
```

### Throwing problems

*Problems* have a loose, yet direct connection to *Exceptions*. Most of the time you'll find yourself transforming one 
into the other. To make this a little bit easier there is an abstract `Problem` implementation that subclasses 
`RuntimeException`: the `ThrowableProblem`. It allows to throw problems and is already in use by all default 
implementations. Instead of implementing the `Problem` interface, just inherit from `AbstractThrowableProblem`:

```java
public final class OutOfStockProblem extends AbstractThrowableProblem {
    // constructor
}
```

If you already have an exception class that you want to extend, you should implement the "marker" interface `Exceptional`:

```java
public final class OutOfStockProblem extends BusinessException implements Exceptional
```

The Jackson support module will recognize this interface and deal with the inherited properties from `Throwable` 
accordingly. Note: This interface only exists, because `Throwable` is a concrete class, rather than an interface.

### Handling problems

Reading problems is very specific to the JSON parser in use. This section assumes you're using Jackson, in which case 
reading/parsing problems usually boils down to this:

```java
Problem problem = mapper.readValue(.., Problem.class);
```

If you're using Jackson, please make sure you understand its 
[Polymorphic Deserialization](https://github.com/FasterXML/jackson-docs/wiki/JacksonPolymorphicDeserialization) feature. The supplied 
Jackson module makes heavy use of it. Considering you have a custom problem type `OutOfStockProblem`, you'll need to 
register it as a subtype:

```java
mapper.registerSubtypes(OutOfStockProblem.class);
```
You also need to make sure you assign a `@JsonTypeName` to it and declare a `@JsonCreator`:

```java
@JsonTypeName(OutOfStockProblem.TYPE_VALUE)
public final class OutOfStockProblem implements Problem {

    @JsonCreator
    public OutOfStockProblem(final String product) {
```

Jackson is now able to deserialize specific problems into their respective types. By default, e.g. if a type is not 
associated with a class, it will fallback to a `DefaultProblem`. 

### Catching problems

If you read about [Throwing problems](#throwing-problems) already, you should be familiar with `ThrowableProblem`. 
This can be helpful if you read a problem, as a response from a server, and what to find out what it actually is. 
Multiple `if` statements with `instanceof` checks could be an option, but usually nicer is this:

```java
try {
    throw mapper.readValue(.., ThrowableProblem.class);
} catch (OutOfStockProblem e) {
    tellTheCustomerTheProductIsNoLongerAvailable(e.getProduct());
} catch (InsufficientFundsProblem e) {
    askCustomerToUseDifferentPaymentMethod(e.getBalance(), e.getDebit());
} catch (InvalidCouponProblem e) {
    askCustomerToUseDifferentCoupon(e.getCouponCode());
} catch (ThrowableProblem e) {
    tellTheCustomerSomethingWentWrong();
}
```

If you used the `Exceptional` interface rather than `ThrowableProblem` you have to adjust your code a little bit:

```java
try {
    throw mapper.readValue(.., Exceptional.class).propagate();
} catch (OutOfStockProblem e) {
    ...
```

### Stack traces and causal chains

Exceptions in Java can be chained/nested using *causes*. `ThrowableProblem` adapts the pattern seamlessly to problems:

```java
ThrowableProblem problem = Problem.builder()
    .withType(URI.create("https://example.org/order-failed"))
    .withTitle("Order failed")
    .withStatus(BAD_REQUEST)
    .withCause(Problem.builder()
      .withType(URI.create("https://example.org/out-of-stock"))
      .withTitle("Out of Stock")
      .withStatus(BAD_REQUEST)
      .build())
    .build();
    
problem.getCause(); // standard API of java.lang.Throwable
```

Will produce this:

```json
{
  "type": "https://example.org/order-failed",
  "title": "Order failed",
  "status": 400,
  "cause": {
    "type": "https://example.org/out-of-stock",
    "title": "Out of Stock",
    "status": 400,
    "detail": "Item B00027Y5QG is no longer available"
  }
}
```

Another important aspect of exceptions are stack traces, but since they leak implementation details to the outside world, 
[**we strongly advise against exposing them**](http://zalando.github.io/restful-api-guidelines/#177)
in problems. That being said, there is a legitimate use case when you're debugging an issue on an integration environment
and you don't have direct access to the log files. Serialization of stack traces can be enabled on the problem module:

```java
ObjectMapper mapper = new ObjectMapper()
    .registerModule(new ProblemModule().withStackTraces());
```

After enabling stack traces all problems will contain a `stacktrace` property:

```json
{
  "type": "about:blank",
  "title": "Unprocessable Entity",
  "status": 400,
  "stacktrace": [
    "org.example.Example.execute(Example.java:17)",
    "org.example.Example.main(Example.java:11)"
  ]
}
```

Since we discourage the  serialization of them, there is currently, by design, no way to deserialize them from JSON.
Nevertheless the runtime will fill in the stack trace when the problem instance is created. That stack trace is usually
not 100% correct, since it looks like the exception originated inside your deserialization framework. *Problem* comes
with a special service provider interface `StackTraceProcessor` that can be registered using the 
[`ServiceLoader` capabilities](http://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html). It can be used
to modify the stack trace, e.g. remove all lines before your own client code, e.g. Jackson/HTTP client/etc.

```java
public interface StackTraceProcessor {

    Collection<StackTraceElement> process(final Collection<StackTraceElement> elements);

}
```

By default no processing takes place.

## Getting help

If you have questions, concerns, bug reports, etc, please file an issue in this repository's Issue Tracker.

## Getting involved

To contribute, simply make a pull request and add a brief description (1-2 sentences) of your addition or change. 
For more details check the [contribution guidelines](.github/CONTRIBUTING.md).

## Credits and references

### Spring Framework

Users of the [Spring Framework](https://spring.io) are highly encouraged to check out [Problems for Spring Web MVC](https://github.com/zalando/problem-spring-web), a library that seemlessly integrates problems into Spring.

### Micronaut Framework
Users of the [Micronaut Framework](https://micronaut.io) are highly encouraged to check out [Micronaut Problem JSON
](https://micronaut-projects.github.io/micronaut-problem-json/snapshot/guide/), a library that seemlessly integrates problems into Micronaut error processing.

### Quarkus Framework
Users of the [Quarkus Framework](https://quarkus.io/) are highly encouraged to check out [Quarkus RESTeasy Problem Extension](https://github.com/TietoEVRY/quarkus-resteasy-problem/), a library that seemlessly integrates problems into Quarkus RESTeasy/JaxRS error processing. It also handles Problem-family exceptions from `org.zalando:problem` library.
