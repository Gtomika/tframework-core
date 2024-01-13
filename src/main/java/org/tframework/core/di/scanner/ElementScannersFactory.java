/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.scanner;

import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.tframework.core.annotations.AnnotationScannersFactory;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.reflection.classes.ClassFiltersFactory;
import org.tframework.core.reflection.classes.ClassScannersFactory;
import org.tframework.core.reflection.methods.MethodFiltersFactory;
import org.tframework.core.reflection.methods.MethodScannersFactory;

/**
 * Factory for creating various {@link ElementScanner}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ElementScannersFactory {

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

    /**
     * Creates a new {@link PackagesElementClassScanner} to scan the packages specified in the
     * {@value PackagesElementClassScanner#SCAN_PACKAGES_PROPERTY} property.
     * @param properties Properties container to check which packages are to be scanned.
     */
    public static PackagesElementClassScanner createPackagesElementClassScanner(PropertiesContainer properties) {
        return PackagesElementClassScanner.builder()
                .annotationScanner(AnnotationScannersFactory.createComposedAnnotationScanner())
                .classFilter(ClassFiltersFactory.createDefaultClassFilter())
                .classScanner(ClassScannersFactory.createPackageClassScanner())
                .propertiesContainer(properties)
                .build();
    }

    /**
     * Creates a new {@link ClassesElementClassScanner} to scan the classes specified in the
     * {@value ClassesElementClassScanner#SCAN_CLASSES_PROPERTY} property.
     */
    public static ClassesElementClassScanner createClassesElementClassScanner(PropertiesContainer properties) {
        return ClassesElementClassScanner.builder()
                .annotationScanner(AnnotationScannersFactory.createComposedAnnotationScanner())
                .classFilter(ClassFiltersFactory.createDefaultClassFilter())
                .propertiesContainer(properties)
                .build();
    }

    /**
     * Creates a {@link FixedClassesElementMethodScanner} with default configuration that scans elements
     * from the methods of a class set.
     * @param classesToScan Classes to scan, not null.
     */
    public static FixedClassesElementMethodScanner createFixedClassesElementMethodScanner(@NonNull Set<Class<?>> classesToScan) {
        return FixedClassesElementMethodScanner.builder()
                .classesToScan(classesToScan)
                .methodScanner(MethodScannersFactory.createDefaultMethodScanner())
                .methodFilter(MethodFiltersFactory.createDefaultMethodFilter())
                .annotationScanner(AnnotationScannersFactory.createComposedAnnotationScanner())
                .build();
    }

}
