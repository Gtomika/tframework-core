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

import org.tframework.core.Application;
import org.tframework.core.elements.annotations.Element;

@Element
public class DependencyResolverConfig {

    public static final String FIELD_DEPENDENCY_RESOLVER_ELEMENT_NAME = "fieldDependencyResolver";
    public static final String PARAMETER_DEPENDENCY_RESOLVER_ELEMENT_NAME = "parameterDependencyResolver";

    private final DependencyResolutionInput dependencyResolutionInput;

    public DependencyResolverConfig(Application application) {
        this.dependencyResolutionInput = DependencyResolutionInput.builder()
                .application(application)
                .build();
    }

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
