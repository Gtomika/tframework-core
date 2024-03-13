/* Licensed under Apache-2.0 2024. */
package org.tframework.properties.files;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectProperty;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

/*
The framework is expected to pick up the default property file 'properties.yaml' from the
resources folder.
 */
@IsolatedTFrameworkTest
public class DefaultPropertyFileTest {

    @Test
    public void shouldReadPropertiesFromDefaultPropertyFile(
            @InjectProperty("integration-test.default.property") Integer defaultProperty
    ) {
        assertEquals(1, defaultProperty);
    }
}
