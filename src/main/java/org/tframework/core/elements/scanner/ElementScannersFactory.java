/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.scanner;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.tframework.core.elements.ElementsInitializationInput;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.reflection.annotations.AnnotationScannersFactory;
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
     * Creates an {@link ElementContextBundle} with default {@link ElementScanner}s.
     * @param input {@link ElementsInitializationInput}, not null.
     */
    public static ElementContextBundle createDefaultElementScannersBundle(@NonNull ElementsInitializationInput input) {
        return ElementContextBundle.builder()
                .elementClassScanners(createDefaultElementClassScanners(input))
                .elementMethodScanners(createDefaultElementMethodScanners(input))
                .build();
    }

    /**
     * Creates a list of {@link ElementScanner}s that scan for elements that are defined as classes, annotated
     * with {@link org.tframework.core.elements.annotations.Element}. Some scanners are enabled/disabled based on the
     * properties in the {@code input}.
     * @param input {@link ElementsInitializationInput}, not null.
     */
    public static List<ElementClassScanner> createDefaultElementClassScanners(@NonNull ElementsInitializationInput input) {
        var propertiesContainer = input.application().getPropertiesContainer();
        return List.of(
                createRootElementClassScanner(input.rootClass(), propertiesContainer),
                createInternalElementClassScanner(propertiesContainer),
                createPackagesElementClassScanner(propertiesContainer),
                createClassesElementClassScanner(propertiesContainer)
        );
    }

    /**
     * Creates a list of {@link ElementScanner}s that scan for elements that are defined as methods, annotated
     * with {@link org.tframework.core.elements.annotations.Element}. These classes are typically discovered by the
     * element scanners of {@link #createDefaultElementClassScanners(ElementsInitializationInput)}.
     * @param input {@link ElementsInitializationInput}, not null.
     */
    public static List<ElementMethodScanner> createDefaultElementMethodScanners(@NonNull ElementsInitializationInput input) {
        //input is not needed yet, but it might be in the future
        return List.of(createFixedClassesElementMethodScanner());
    }

    /**
     * Creates a new {@link RootElementClassScanner} to scan a root class' package and subpackages.
     * @param rootClass Root class that the scanner should use.
     * @param properties Properties container to check if scanning should be enabled/disabled.
     */
     static RootElementClassScanner createRootElementClassScanner(Class<?> rootClass, PropertiesContainer properties) {
        return RootElementClassScanner.builder()
                .annotationScanner(AnnotationScannersFactory.createComposedAnnotationScanner())
                .classFilter(ClassFiltersFactory.createDefaultClassFilter())
                .packageClassScanner(ClassScannersFactory.createPackageClassScanner())
                .rootClassScanner(ClassScannersFactory.createNestedClassScanner(rootClass))
                .propertiesContainer(properties)
                .rootClass(rootClass)
                .build();
    }

    /**
     * Creates a new {@link InternalElementClassScanner} to scan the internal framework packages.
     * @param properties Properties container to check if scanning should be enabled/disabled.
     */
     static InternalElementClassScanner createInternalElementClassScanner(PropertiesContainer properties) {
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
     static PackagesElementClassScanner createPackagesElementClassScanner(PropertiesContainer properties) {
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
     static ClassesElementClassScanner createClassesElementClassScanner(PropertiesContainer properties) {
        return ClassesElementClassScanner.builder()
                .annotationScanner(AnnotationScannersFactory.createComposedAnnotationScanner())
                .classFilter(ClassFiltersFactory.createDefaultClassFilter())
                .propertiesContainer(properties)
                .build();
    }

    /**
     * Creates a {@link FixedClassesElementMethodScanner} with default configuration that scans elements
     * from the methods of a class set.
     */
     static FixedClassesElementMethodScanner createFixedClassesElementMethodScanner() {
        return FixedClassesElementMethodScanner.builder()
                .methodScanner(MethodScannersFactory.createDefaultMethodScanner())
                .methodFilter(MethodFiltersFactory.createDefaultMethodFilter())
                .annotationScanner(AnnotationScannersFactory.createComposedAnnotationScanner())
                .build();
    }

}
