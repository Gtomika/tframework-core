/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.properties.converters.PropertyConverterAggregator;

@ExtendWith(MockitoExtension.class)
class PropertiesContainerTest {

    @Mock
    private PropertyConverterAggregator aggregator;

    private PropertiesContainer container;

    @BeforeEach
    public void setUp() {
        container = new PropertiesContainer(List.of(
                new Property("p1", new SinglePropertyValue("v1")),
                new Property("p2", new ListPropertyValue(List.of("v2-1", "v2-2"))
        )), aggregator);
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
        when(aggregator.convert(any(PropertyValue.class), eq(String.class))).thenReturn("v1");
        assertEquals("v1", container.getPropertyValue("p1", String.class));
    }

    @Test
    public void shouldThrowException_getPropertyValue_whenPropertyDoesNotExist() {
        var exception = assertThrows(PropertyNotFoundException.class, () -> {
            container.getPropertyValue("p3", String.class);
        });

        assertEquals(
                exception.getMessageTemplate().formatted("p3"),
                exception.getMessage()
        );
    }

    @Test
    public void shouldGetPropertyValue_whenExists_withDefaultValue() {
        when(aggregator.convert(any(PropertyValue.class), eq(String.class))).thenReturn("v1");

        assertEquals("v1", container.getPropertyValue("p1", String.class, "default"));
    }

    @Test
    public void shouldGetDefaultValue_getPropertyValue_whenPropertyDoesNotExist() {
        assertEquals("default", container.getPropertyValue("p3", String.class, "default"));
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
        PropertiesContainer newContainer = container.merge(List.of(
                new Property("p2", new SinglePropertyValue("v2-override")),
                new Property("p3", new SinglePropertyValue("v3"))
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
