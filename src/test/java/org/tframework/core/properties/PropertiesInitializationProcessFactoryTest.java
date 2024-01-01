/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class PropertiesInitializationProcessFactoryTest {

    @Test
    void shouldNotCreatePropertiesInitializationProcess_whenClasspathIsMissingDependencies() {
        var process = PropertiesInitializationProcessFactory.createProfileInitializationProcess();
        assertNotNull(process);
    }
}
