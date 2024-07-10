/*
Copyright 2024 Tamas Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.tframework.core.elements.dependency.resolver;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.dependency.InjectAnnotationScanner;
import org.tframework.core.elements.dependency.handler.SpecialElementDependencyHandlerFactory;
import org.tframework.core.elements.dependency.resolver.helper.ElementByNameResolverHelper;
import org.tframework.core.elements.dependency.resolver.helper.ElementByTypeResolverHelper;
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
        var handlerAggregator = SpecialElementDependencyHandlerFactory.createDefaultHandlerAggregator();
        return new AnnotatedElementDependencyResolver(
                elementsContainer,
                InjectAnnotationScanner.wrappingScanner(annotationScanner),
                new ElementByNameResolverHelper(),
                new ElementByTypeResolverHelper(),
                handlerAggregator
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
        var handlerAggregator = SpecialElementDependencyHandlerFactory.createDefaultHandlerAggregator();
        return new FallbackDependencyResolver(
                elementsContainer,
                new ElementByTypeResolverHelper(),
                handlerAggregator
        );
    }

}
