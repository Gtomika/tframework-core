/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.scanner;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.tframework.core.annotations.AnnotationScanner;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.reflection.methods.MethodFilter;
import org.tframework.core.reflection.methods.MethodScanner;

/**
 * A {@link ElementScanner} that is able to find {@link Element}s
 * from the methods a fixed collection classes, provided at construction time.
 */
@Builder
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class FixedClassesElementMethodScanner extends ElementMethodScanner {

    private final MethodScanner methodScanner;
    private final MethodFilter methodFilter;
    private final AnnotationScanner annotationScanner;

    @Override
    public Set<ElementScanningResult<Method>> scanElements() {
        var methods = methodScanner.scanMethods(classToScan);

        //strict filtering: if a method has more than one @Element annotation, throw an exception
        return methodFilter.filterByAnnotation(methods, Element.class, annotationScanner, true)
                .stream()
                .map(result -> new ElementScanningResult<>(result.annotation(), result.annotationSource()))
                .collect(Collectors.toSet());
    }
}
