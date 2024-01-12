/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.scanner;

import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.tframework.core.annotations.AnnotationScannersFactory;
import org.tframework.core.reflection.methods.MethodFiltersFactory;
import org.tframework.core.reflection.methods.MethodScannersFactory;

/**
 * A factory for creating {@link ElementMethodScanner}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ElementMethodScannersFactory {

    /**
     * Creates a {@link FixedClassesElementMethodScanner} with default configuration that scans elements
     * from the methods of a class set.
     * @param classesToScan Classes to scan, not null.
     */
    public static FixedClassesElementMethodScanner createClassSetElementMethodScanner(@NonNull Set<Class<?>> classesToScan) {
        return FixedClassesElementMethodScanner.builder()
                .classesToScan(classesToScan)
                .methodScanner(MethodScannersFactory.createDefaultMethodScanner())
                .methodFilter(MethodFiltersFactory.createDefaultMethodFilter())
                .annotationScanner(AnnotationScannersFactory.createComposedAnnotationScanner())
                .build();
    }

}
