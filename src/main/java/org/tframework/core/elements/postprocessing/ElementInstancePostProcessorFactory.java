/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.postprocessing;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.elements.dependency.InjectAnnotationScanner;
import org.tframework.core.elements.dependency.resolver.DependencyResolutionInput;
import org.tframework.core.elements.dependency.resolver.DependencyResolverAggregator;
import org.tframework.core.elements.dependency.resolver.DependencyResolversFactory;
import org.tframework.core.reflection.annotations.AnnotationScannersFactory;
import org.tframework.core.reflection.field.FieldFiltersFactory;
import org.tframework.core.reflection.field.FieldScannersFactory;
import org.tframework.core.reflection.field.FieldSettersFactory;

/**
 * Creates {@link ElementInstancePostProcessor}s and {@link ElementInstancePostProcessorAggregator}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ElementInstancePostProcessorFactory {

    /**
     * Creates the default {@link ElementInstancePostProcessorAggregator} of the framework.
     * @param postProcessingInput {@link PostProcessingInput} with the required data to create the post-processors.
     */
    public static ElementInstancePostProcessorAggregator createDefaultAggregator(PostProcessingInput postProcessingInput) {
        List<ElementInstancePostProcessor> processors = List.of(
                createFieldInjectionPostProcessor(postProcessingInput.dependencyResolutionInput())
        );
        return ElementInstancePostProcessorAggregator.usingPostProcessors(processors);
    }

    private static FieldInjectionPostProcessor createFieldInjectionPostProcessor(DependencyResolutionInput dependencyResolutionInput) {
        var fieldDependencyResolver = DependencyResolverAggregator.usingResolvers(
                DependencyResolversFactory.createFieldDependencyResolvers(dependencyResolutionInput)
        );
        var annotationScanner = AnnotationScannersFactory.createComposedAnnotationScanner();

        return FieldInjectionPostProcessor.builder()
                .dependencyResolver(fieldDependencyResolver)
                .injectAnnotationScanner(InjectAnnotationScanner.wrappingScanner(annotationScanner))
                .fieldScanner(FieldScannersFactory.createDefaultFieldScanner())
                .fieldFilter(FieldFiltersFactory.createDefaultFieldFilter())
                .fieldSetter(FieldSettersFactory.createDefaultFieldSetter())
                .build();
    }

}
