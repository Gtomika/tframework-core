/* Licensed under Apache-2.0 2023. */
package org.tframework.core.initializers;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class CoreInitializationFactoryTest {

    //happy path is tested in snackYamlTest and jackYamlTest sets
    @Test
    public void shouldCreateCoreInitializationProcess() {
        var process = CoreInitializationFactory.createCoreInitializationProcess();
        assertNotNull(process);
    }

}
