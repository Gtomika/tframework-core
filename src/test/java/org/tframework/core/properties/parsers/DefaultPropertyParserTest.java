/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.tframework.core.properties.ListPropertyValue;
import org.tframework.core.properties.Property;
import org.tframework.core.properties.SinglePropertyValue;

class DefaultPropertyParserTest {

    private final DefaultPropertyParser propertyParser = new DefaultPropertyParser();

    @Test
    public void shouldParseRawProperty_intoSingleValuedProperty() {
        String rawProperty = "cool.property=test";
        Property property = propertyParser.parseProperty(rawProperty);

        assertEquals("cool.property", property.name());
        if(property.value() instanceof SinglePropertyValue spv) {
            assertEquals("test", spv.value());
        } else {
            fail("Single property value expected");
        }
    }

    @Test
    public void shouldParseRawProperty_intoListValuedProperty() {
        String rawProperty = "cool.list.property=[t1,t2]";
        Property property = propertyParser.parseProperty(rawProperty);

        assertEquals("cool.list.property", property.name());
        if(property.value() instanceof ListPropertyValue spv) {
            assertEquals(List.of("t1", "t2"), spv.values());
        } else {
            fail("List property value expected");
        }
    }

    @Test
    public void shouldThrowPropertyParsingException_whenPropertyNameIsEmpty() {
        String rawProperty = "   =test";
        var exception = assertThrows(PropertyParsingException.class, () -> propertyParser.parseProperty(rawProperty));

        assertEquals(
                exception.getMessageTemplate().formatted(rawProperty, DefaultPropertyParser.BLANK_NAME_ERROR),
                exception.getMessage()
        );
    }

}
