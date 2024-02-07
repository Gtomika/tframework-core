package org.tframework.test.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.properties.parsers.PropertyParsingUtils;
import org.tframework.core.properties.parsers.SeparatedProperty;
import org.tframework.core.properties.scanners.SystemPropertyScanner;

import java.util.List;
import java.util.stream.Collectors;

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

    public static void setRawFrameworkPropertyIntoSystemProperties(String rawProperty) {
        SeparatedProperty separatedProperty = PropertyParsingUtils.separateNameValue(rawProperty);
        setFrameworkPropertyIntoSystemProperties(separatedProperty.name(), separatedProperty.value());
    }

    public static void setFrameworkPropertyIntoSystemProperties(String name, String... values) {
        String value = PropertyParsingUtils.LIST_BEGIN_CHARACTER +
                String.join(PropertyParsingUtils.LIST_ELEMENT_SEPARATOR_CHARACTER, values) +
                PropertyParsingUtils.LIST_END_CHARACTER;
        setFrameworkPropertyIntoSystemProperties(name, value);
    }

    public static void setFrameworkPropertyIntoSystemProperties(String name, String value) {
        log.debug("The following framework property will be set into the system properties: {} = {}", name, value);
        //if it exists, it will simply be overridden
        System.setProperty(SystemPropertyScanner.PROPERTY_PREFIX + name, value);
    }

}
