/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PropertiesContainerTest {

    private PropertiesContainer container;

    @BeforeEach
    public void setUp() {
        container = PropertiesContainer.fromProperties(Map.of(
                "p1", new SinglePropertyValue("v1"),
                "p2", new ListPropertyValue(List.of("v2-1", "v2-2")
        )));
    }

    @Test
    public void shouldGetPropertyValueObject_whenExists() {
        PropertyValue propertyValue = container.getPropertyValueObject("p1");
        if(propertyValue instanceof SinglePropertyValue(String value)) {
            assertEquals("v1", value);
        } else {
            fail("SinglePropertyValue expected");
        }
    }

    @Test
    public void shouldThrowException_getPropertyValueObject_whenPropertyDoesNotExist() {
        var exception = assertThrows(PropertyNotFoundException.class, () -> {
            container.getPropertyValueObject("p3");
        });

        assertEquals(
                exception.getMessageTemplate().formatted("p3"),
                exception.getMessage()
        );
    }

    @Test
    public void shouldGetPropertyValueObject_whenDoesNotExist_withDefaultValue() {
        PropertyValue propertyValue = container.getPropertyValueObject("p3", new SinglePropertyValue("default"));
        if(propertyValue instanceof SinglePropertyValue(String value)) {
            assertEquals("default", value);
        } else {
            fail("SinglePropertyValue expected");
        }
    }

    @Test
    public void shouldGetPropertyValue_whenExists() {
        assertEquals("v1", container.getPropertyValue("p1"));
        assertEquals("[v2-1, v2-2]", container.getPropertyValue("p2"));
    }

    @Test
    public void shouldThrowException_getPropertyValue_whenPropertyDoesNotExist() {
        var exception = assertThrows(PropertyNotFoundException.class, () -> {
            container.getPropertyValue("p3");
        });

        assertEquals(
                exception.getMessageTemplate().formatted("p3"),
                exception.getMessage()
        );
    }

    @Test
    public void shouldGetPropertyValue_whenExists_withDefaultValue() {
        assertEquals("v1", container.getPropertyValue("p1", "default"));
        assertEquals("[v2-1, v2-2]", container.getPropertyValue("p2", "default"));
    }

    @Test
    public void shouldGetDefaultValue_getPropertyValue_whenPropertyDoesNotExist() {
        assertEquals("default", container.getPropertyValue("p3", "default"));
    }

    @Test
    public void shouldGetPropertyValueList_whenExists() {
        assertEquals(List.of("v1"), container.getPropertyValueList("p1"));
        assertEquals(List.of("v2-1", "v2-2"), container.getPropertyValueList("p2"));
    }

    @Test
    public void shouldThrowException_getPropertyValueList_whenPropertyDoesNotExist() {
        var exception = assertThrows(PropertyNotFoundException.class, () -> {
            container.getPropertyValueList("p3");
        });

        assertEquals(
                exception.getMessageTemplate().formatted("p3"),
                exception.getMessage()
        );
    }

    @Test
    public void shouldGetPropertyValueList_whenExists_withDefaultValue() {
        assertEquals(List.of("default"), container.getPropertyValueList("p3", List.of("default")));
    }

    @Test
    public void shouldMergeProperties_andCreateNewContainer() {
        PropertiesContainer newContainer = container.merge(Map.of(
                "p2", new SinglePropertyValue("v2-override"),
                "p3", new SinglePropertyValue("v3")
        ));

        assertEquals("v1", ((SinglePropertyValue) newContainer.getPropertyValueObject("p1")).value());
        assertEquals("v2-override", ((SinglePropertyValue) newContainer.getPropertyValueObject("p2")).value());
        assertEquals("v3", ((SinglePropertyValue) newContainer.getPropertyValueObject("p3")).value());
    }

    @Test
    public void shouldGetPropertiesSize() {
        assertEquals(2, container.size());
    }

    @Test
    public void shouldCreateStringRepresentation() {
        assertEquals("""
                Properties container with the following properties:
                 - p1: v1
                 - p2: [v2-1, v2-2]""", container.toString());
    }

}
