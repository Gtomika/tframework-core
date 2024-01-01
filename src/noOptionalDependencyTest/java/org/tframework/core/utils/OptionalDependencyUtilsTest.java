/* Licensed under Apache-2.0 2023. */
package org.tframework.core.utils;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class OptionalDependencyUtilsTest {

    /*
    Test cases where the these dependencies are available are in the 'test' and 'snakeYamlTest' modules.
     */
    @ParameterizedTest
    @EnumSource(value = OptionalDependency.class, names = {"JACKSON_YAML", "SNAKE_YAML"})
    public void shouldReturnFalse_whenOptionalDependencyIsNotAvailable(OptionalDependency optionalDependency) {
        boolean isAvailable = OptionalDependencyUtils.isOptionalDependencyAvailable(optionalDependency);
        assertFalse(isAvailable);
    }

}
