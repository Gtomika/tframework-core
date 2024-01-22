package org.tframework.core.elements.dependency;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.reflection.annotations.AnnotationScannersFactory;

import java.util.List;

/**
 * Utilities to create {@link DependencyResolver}s.
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

    private static ElementDependencyResolver createElementDependencyResolver(ElementsContainer elementsContainer) {
        var annotationScanner = AnnotationScannersFactory.createComposedAnnotationScanner();
        return new ElementDependencyResolver(elementsContainer, new InjectAnnotationScanner(annotationScanner));
    }

    private static PropertyDependencyResolver createPropertyDependencyResolver(PropertiesContainer propertiesContainer) {
        var annotationScanner = AnnotationScannersFactory.createComposedAnnotationScanner();
        return new PropertyDependencyResolver(propertiesContainer, new InjectAnnotationScanner(annotationScanner));
    }

    private static FallbackDependencyResolver createFallbackDependencyResolver(ElementsContainer elementsContainer) {
        return new FallbackDependencyResolver(elementsContainer);
    }

}
