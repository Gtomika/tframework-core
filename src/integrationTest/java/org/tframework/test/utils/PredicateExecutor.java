package org.tframework.test.utils;

import java.util.function.Predicate;
import java.util.function.Supplier;

public record PredicateExecutor<T, R>(
        String name,
        Predicate<T> predicate,
        Supplier<R> action
) {
}