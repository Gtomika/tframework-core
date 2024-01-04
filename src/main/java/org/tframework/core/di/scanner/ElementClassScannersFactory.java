/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.scanner;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.annotations.AnnotationScannersFactory;
import org.tframework.core.classes.ClassFiltersFactory;
import org.tframework.core.classes.ClassScannersFactory;
import org.tframework.core.properties.PropertiesContainer;

/**
 * Factory for creating various {@link ElementClassScanner}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ElementClassScannersFactory {

    /**
     * Creates a new {@link RootElementClassScanner} to scan a root class' package and subpackages.
     * @param rootClass Root class that the scanner should use.
     * @param properties Properties container to check if scanning should be enabled/disabled.
     */
    public static RootElementClassScanner createRootElementClassScanner(Class<?> rootClass, PropertiesContainer properties) {
        return RootElementClassScanner.builder()
                .annotationScanner(AnnotationScannersFactory.createComposedAnnotationScanner())
                .classFilter(ClassFiltersFactory.createDefaultClassFilter())
                .classScanner(ClassScannersFactory.createPackageClassScanner())
                .propertiesContainer(properties)
                .rootClass(rootClass)
                .build();
    }

    /**
     * Creates a new {@link InternalElementClassScanner} to scan the internal framework packages.
     * @param properties Properties container to check if scanning should be enabled/disabled.
     */
    public static InternalElementClassScanner createInternalElementClassScanner(PropertiesContainer properties) {
        return InternalElementClassScanner.builder()
                .annotationScanner(AnnotationScannersFactory.createComposedAnnotationScanner())
                .classFilter(ClassFiltersFactory.createDefaultClassFilter())
                .classScanner(ClassScannersFactory.createPackageClassScanner())
                .propertiesContainer(properties)
                .build();
    }

}
