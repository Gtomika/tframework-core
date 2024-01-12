/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.scanner;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.tframework.core.annotations.AnnotationScanner;
import org.tframework.core.di.annotations.Element;
import org.tframework.core.reflection.methods.MethodFilter;
import org.tframework.core.reflection.methods.MethodScanner;

/**
 * A {@link ElementMethodScanner} that is able to find {@link Element}s
 * from the methods a fixed collection classes, provided at construction time.
 */
@Builder
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class FixedClassesElementMethodScanner implements ElementMethodScanner {

    private final Set<Class<?>> classesToScan;
    private final MethodScanner methodScanner;
    private final MethodFilter methodFilter;
    private final AnnotationScanner annotationScanner;

    @Override
    public Set<Method> scanElements() {
        var methods = classesToScan.stream()
                .flatMap(clazz -> methodScanner.scanMethods(clazz).stream())
                .collect(Collectors.toSet());

        return methodFilter.filterByAnnotation(methods, Element.class, annotationScanner);
    }
}
