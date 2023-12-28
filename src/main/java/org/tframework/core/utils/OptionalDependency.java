/* Licensed under Apache-2.0 2023. */
package org.tframework.core.utils;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumerates the optional dependencies of the framework.
 */
@Getter
@RequiredArgsConstructor
public enum OptionalDependency {

    JACKSON_YAML("com.fasterxml.jackson.dataformat.yaml.YAMLFactory"),
    SNAKE_YAML("org.yaml.snakeyaml.Yaml");

    /**
     * A class that is available if the dependency is on the classpath.
     */
    private final String essentialClassName;

}
