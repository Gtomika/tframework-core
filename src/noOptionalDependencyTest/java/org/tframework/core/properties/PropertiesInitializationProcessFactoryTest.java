/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.tframework.core.properties.parsers.NoYamlParserLibraryException;

class PropertiesInitializationProcessFactoryTest {

    @Test
    void shouldNotCreatePropertiesInitializationProcess_whenClasspathIsMissingDependencies() {
        //this test set has no YAML parser library on the classpath
        assertThrows(NoYamlParserLibraryException.class, PropertiesInitializationProcessFactory::createProfileInitializationProcess);
    }
}
