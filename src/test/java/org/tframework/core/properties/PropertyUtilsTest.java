/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class PropertyUtilsTest {

    private final List<Property> properties = List.of(
        new Property("a", new SinglePropertyValue("a value")),
        new Property("b", new SinglePropertyValue("b value")),
        new Property("c", new SinglePropertyValue("c value"))
    );

    @Test
    public void shouldGetValueFromPropertyList() {
        var value = PropertyUtils.getValueFromPropertyList(properties, "a");
        if(value instanceof SinglePropertyValue(String actualValue)) {
            assertEquals("a value", actualValue);
        } else {
            fail("single property value expected");
        }
    }

    @Test
    public void shouldThrowNotFoundException_whenPropertyDoesNotExist() {
        var exception = assertThrows(PropertyNotFoundException.class, () -> {
            PropertyUtils.getValueFromPropertyList(properties, "x");
        });

        assertEquals(
                exception.getMessageTemplate().formatted("x"),
                exception.getMessage()
        );
    }

    @Test
    public void shouldReplacePropertyInList() {
        var propertiesCopy = new ArrayList<>(properties);
        var newProperty = new Property("a", new SinglePropertyValue("updated a"));

        PropertyUtils.replaceValueInPropertyList(propertiesCopy, newProperty);

        var value = PropertyUtils.getValueFromPropertyList(propertiesCopy, "a");
        if(value instanceof SinglePropertyValue(String actualValue)) {
            assertEquals("updated a", actualValue);
        } else {
            fail("single property value expected");
        }
    }
}
