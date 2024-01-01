/* Licensed under Apache-2.0 2023. */
package org.tframework.core.initializers;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.tframework.core.properties.parsers.NoYamlParserLibraryException;

class CoreInitializationFactoryTest {

    @Test
    public void shouldCreateCoreInitializationProcess() {
        assertThrows(NoYamlParserLibraryException.class,
                CoreInitializationFactory::createCoreInitializationProcess);
    }

}
