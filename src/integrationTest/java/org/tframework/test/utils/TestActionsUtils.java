/* Licensed under Apache-2.0 2024. */
package org.tframework.test.utils;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestActionsUtils {

    public static <T, R> R executeIfExactlyOneIsTrue(T testObject, List<PredicateExecutor<T, R>> predicateExecutors) throws IllegalArgumentException {
        long truePredicates = predicateExecutors.stream()
                .filter(predicateExecutor -> predicateExecutor.predicate().test(testObject))
                .count();

        if(truePredicates != 1) {
            String names = predicateExecutors.stream()
                    .map(PredicateExecutor::name)
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Exactly one of these must be true: " + names);
        }

        var executor = predicateExecutors.stream()
                .filter(predicateExecutor -> predicateExecutor.predicate().test(testObject))
                .findFirst()
                .orElseThrow();
        return executor.action().get();
    }

}
