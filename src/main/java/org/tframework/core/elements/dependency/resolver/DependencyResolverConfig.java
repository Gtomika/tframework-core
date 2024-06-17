/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency.resolver;

import lombok.RequiredArgsConstructor;
import org.tframework.core.elements.annotations.Element;

@Element
@RequiredArgsConstructor
public class DependencyResolverConfig {

    public static final String FIELD_DEPENDENCY_RESOLVER_ELEMENT_NAME = "fieldDependencyResolver";
    public static final String PARAMETER_DEPENDENCY_RESOLVER_ELEMENT_NAME = "parameterDependencyResolver";

    private final DependencyResolutionInput dependencyResolutionInput;

    @Element(name = FIELD_DEPENDENCY_RESOLVER_ELEMENT_NAME)
    public DependencyResolverAggregator provideFieldDependencyResolver() {
        var resolvers = DependencyResolversFactory.createFieldDependencyResolvers(dependencyResolutionInput);
        return DependencyResolverAggregator.usingResolvers(resolvers);
    }

    @Element(name = PARAMETER_DEPENDENCY_RESOLVER_ELEMENT_NAME)
    public DependencyResolverAggregator provideParameterDependencyResolver() {
        var resolvers = DependencyResolversFactory.createParameterDependencyResolvers(dependencyResolutionInput);
        return DependencyResolverAggregator.usingResolvers(resolvers);
    }

}
