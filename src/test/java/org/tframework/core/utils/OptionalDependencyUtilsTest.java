/* Licensed under Apache-2.0 2023. */
package org.tframework.core.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class OptionalDependencyUtilsTest {

    @Test
    public void shouldReturnTrue_whenJacksonYamlOptionalDependencyIsOnClasspath() {
        boolean isAvailable = OptionalDependencyUtils.isOptionalDependencyAvailable(OptionalDependency.JACKSON_YAML);
        assertTrue(isAvailable);
    }

}
