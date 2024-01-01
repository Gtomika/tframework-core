/* Licensed under Apache-2.0 2023. */
package org.tframework.core.utils;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class OptionalDependencyUtilsTest {

    /*
    Test cases where the these dependencies are not available or available are in the 'test' and 'snakeYamlTest' modules.
     */
    @Test
    public void shouldReturnTrue_whenJacksonYamlOptionalDependencyIsOnClasspath() {
        boolean isAvailable = OptionalDependencyUtils.isOptionalDependencyAvailable(OptionalDependency.JACKSON_YAML);
        assertTrue(isAvailable);
    }

}
