/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency.resolver;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.dependency.InjectAnnotationScanner;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.reflection.annotations.AnnotationScannersFactory;

/**
 * Utilities to create {@link BasicDependencyResolver}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DependencyResolversFactory {

    /**
     * Creates a list of {@link DependencyResolver}s that can resolve dependencies for parameters, such as
     * constructors when creating elements, or when element methods are invoked.
     */
    public static List<DependencyResolver> createParameterDependencyResolvers(DependencyResolutionInput input) {
        //the order is important here! fallback must be the last
        return List.of(
                createElementDependencyResolver(input.elementsContainer()),
                createPropertyDependencyResolver(input.propertiesContainer()),
                createFallbackDependencyResolver(input.elementsContainer())
        );
    }

    /**
     * Creates a list of {@link DependencyResolver}s that can resolve dependencies for fields, for example when
     * elements are initialized.
     */
    public static List<DependencyResolver> createFieldDependencyResolvers(DependencyResolutionInput input) {
        //the order is important here! fallback is not present, fields must be explicitly annotated
        return List.of(
                createElementDependencyResolver(input.elementsContainer()),
                createPropertyDependencyResolver(input.propertiesContainer())
        );
    }

    public static AnnotatedElementDependencyResolver createElementDependencyResolver(ElementsContainer elementsContainer) {
        var annotationScanner = AnnotationScannersFactory.createComposedAnnotationScanner();
        return new AnnotatedElementDependencyResolver(
                elementsContainer,
                InjectAnnotationScanner.wrappingScanner(annotationScanner)
        );
    }

    public static PropertyDependencyResolver createPropertyDependencyResolver(PropertiesContainer propertiesContainer) {
        var annotationScanner = AnnotationScannersFactory.createComposedAnnotationScanner();
        return new PropertyDependencyResolver(
                propertiesContainer,
                InjectAnnotationScanner.wrappingScanner(annotationScanner)
        );
    }

    public static FallbackDependencyResolver createFallbackDependencyResolver(ElementsContainer elementsContainer) {
        return new FallbackDependencyResolver(elementsContainer);
    }

}
