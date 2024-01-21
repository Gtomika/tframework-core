/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class PropertyDependencyResolver implements DependencyResolver {

    private final InjectAnnotationScanner injectAnnotationScanner;

    @Override
    public Optional<Object> resolveDependency(DependencySource dependencySource, DependencyDefinition dependencyDefinition) {
        return Optional.empty();
    }
}
