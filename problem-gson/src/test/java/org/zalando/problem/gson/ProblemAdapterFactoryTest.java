package org.zalando.problem.gson;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.zalando.problem.Status;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProblemAdapterFactoryTest {

    @Test
    void defaultConstructorShouldBuildIndexCorrectly() {
        assertDoesNotThrow((ThrowingSupplier<ProblemAdapterFactory>) ProblemAdapterFactory::new);
    }

    @Test
    void shouldThrowForDuplicateStatusCode() {
        assertThrows(IllegalArgumentException.class, () -> new ProblemAdapterFactory(Status.class, CustomStatus.class));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void shouldNotThrowForDuplicateSubtype() {
        assertDoesNotThrow(() -> {
            ProblemAdapterFactory factory = new ProblemAdapterFactory();
            factory.registerSubtype(OutOfStockException.TYPE, OutOfStockException.class)
                    .registerSubtype(InsufficientFundsProblem.TYPE, OutOfStockException.class);
        });
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Test
    void shouldThrowForDuplicateURI() {
        assertThrows(IllegalArgumentException.class, () -> {
            ProblemAdapterFactory factory = new ProblemAdapterFactory();
            factory.registerSubtype(OutOfStockException.TYPE, OutOfStockException.class)
                    .registerSubtype(OutOfStockException.TYPE, InsufficientFundsProblem.class);
        });
    }
}
