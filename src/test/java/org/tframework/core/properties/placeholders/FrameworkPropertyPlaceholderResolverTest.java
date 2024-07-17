package org.tframework.core.properties.placeholders;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.properties.ListPropertyValue;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.properties.PropertyNotFoundException;
import org.tframework.core.properties.SinglePropertyValue;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FrameworkPropertyPlaceholderResolverTest {

    public static final String PROP_NAME = "PROP_NAME";
    public static final String PROP_VALUE = "PROP_VALUE";
    public static final String PROP_DEFAULT_VALUE = "PROP_DEFAULT_VALUE";

    @Mock
    private PropertiesContainer propertiesContainer;

    private final FrameworkPropertyPlaceholderResolver resolver = new FrameworkPropertyPlaceholderResolver();

    @Test
    public void shouldNotResolveProperty_whenNoPropertyPlaceholder() {
        String original = "nothing to resolve";
        String resolved = resolver.resolvePlaceholders(original, propertiesContainer);
        assertEquals(original, resolved);
    }

    @Test
    public void shouldResolveProperty_whenItExists_andSingleValued() {
        when(propertiesContainer.getPropertyValueObject(PROP_NAME))
                .thenReturn(new SinglePropertyValue(PROP_VALUE));
        String original = "this is prop{" + PROP_NAME + "}";

        String resolved = resolver.resolvePlaceholders(original, propertiesContainer);

        assertEquals("this is " + PROP_VALUE, resolved);
    }

    @Test
    public void shouldResolveProperty_whenItExists_andListValued() {
        when(propertiesContainer.getPropertyValueObject(PROP_NAME))
                .thenReturn(new ListPropertyValue(List.of(PROP_VALUE, PROP_VALUE)));
        String original = "this is prop{" + PROP_NAME + "}";

        String resolved = resolver.resolvePlaceholders(original, propertiesContainer);

        assertEquals("this is " + PROP_VALUE + "," + PROP_VALUE, resolved);
    }

    @Test
    public void shouldThrowException_whenPropertyDoesNotExist() {
        when(propertiesContainer.getPropertyValueObject(PROP_NAME))
                .thenThrow(new PropertyNotFoundException(PROP_NAME));
        String original = "this is prop{" + PROP_NAME + "}";

        assertThrows(PropertyNotFoundException.class,
                () -> resolver.resolvePlaceholders(original, propertiesContainer));
    }

    @Test
    public void shouldResolveProperty_whenPropertyDoesNotExist_andDefaultValueProvided() {
        when(propertiesContainer.getPropertyValueObject(PROP_NAME))
                .thenThrow(new PropertyNotFoundException(PROP_NAME));
        String original = "this is prop{" + PROP_NAME + "|" + PROP_DEFAULT_VALUE + "}";

        String resolved = resolver.resolvePlaceholders(original, propertiesContainer);

        assertEquals("this is " + PROP_DEFAULT_VALUE, resolved);
    }
}