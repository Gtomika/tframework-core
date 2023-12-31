/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.tframework.core.properties.parsers.NoYamlParserLibraryException;

class PropertiesInitializationProcessFactoryTest {

    @Test
    void shouldNotCreatePropertiesInitializationProcess_whenClasspathIsMissingDependencies() {
        //this test set has no YAML parser library on the classpath
        //happy path is tested in the other test sets: 'snakeYamlTest' and 'jacksonYamlTest'
        assertThrows(NoYamlParserLibraryException.class, PropertiesInitializationProcessFactory::createProfileInitializationProcess);
    }
}
