/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency;

/**
 * A dependency source is an object that can provide dependency values.
 */
public interface DependencySource {

    /**
     * Requests a dependency from the source. The source may throw an exception if the dependency is not found.
     * @param dependencyName The name of the dependency to request.
     * @return The dependency value.
     */
    Object requestDependency(String dependencyName);

}
