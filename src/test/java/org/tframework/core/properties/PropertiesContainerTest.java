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
    public void shouldGetProperty_whenExists() {
        PropertyValue propertyValue = container.getPropertyValue("p1");
        if(propertyValue instanceof SinglePropertyValue singleValue) {
            assertEquals("v1", singleValue.value());
        } else {
            fail("SinglePropertyValue expected");
        }
    }

    @Test
    public void shouldThrowException_whenPropertyDoesNotExist() {
        var exception = assertThrows(PropertyNotFoundException.class, () -> {
            container.getPropertyValue("p3");
        });

        assertEquals(
                exception.getMessageTemplate().formatted("p3"),
                exception.getMessage()
        );
    }

    @Test
    public void shouldMergeProperties_andCreateNewContainer() {
        PropertiesContainer newContainer = container.merge(Map.of(
                "p2", new SinglePropertyValue("v2-override"),
                "p3", new SinglePropertyValue("v3")
        ));

        assertEquals("v1", ((SinglePropertyValue) newContainer.getPropertyValue("p1")).value());
        assertEquals("v2-override", ((SinglePropertyValue) newContainer.getPropertyValue("p2")).value());
        assertEquals("v3", ((SinglePropertyValue) newContainer.getPropertyValue("p3")).value());
    }

}
