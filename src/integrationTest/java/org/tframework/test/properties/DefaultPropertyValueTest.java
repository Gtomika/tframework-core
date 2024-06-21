/* Licensed under Apache-2.0 2024. */
package org.tframework.test.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectProperty;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@IsolatedTFrameworkTest
public class DefaultPropertyValueTest {

    @InjectProperty(value = "some.non.existing.property", defaultValue = "default")
    private String stringProperty;

    @InjectProperty(value = "some.non.existing.property", defaultValue = "123")
    private int intProperty;

    @Test
    public void shouldInjectDefaultStringProperty() {
        assertEquals("default", stringProperty);
    }

    @Test
    public void shouldInjectDefaultIntProperty() {
        assertEquals(123, intProperty);
    }
}
