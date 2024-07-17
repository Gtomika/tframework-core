package org.tframework.core.properties.placeholders;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.properties.ListPropertyValue;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.properties.PropertiesContainerFactory;
import org.tframework.core.properties.Property;
import org.tframework.core.properties.SinglePropertyValue;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PropertyPlaceholderResolverAggregatorTest {

    private static final String NAME = "property-name";
    private static final String UNRESOLVED_VALUE = "unresolved";
    private static final String RESOLVED_VALUE = "resolved";

    private static final Property UNRESOLVED_SINGLE_PROPERTY = Property.builder()
            .name(NAME)
            .value(new SinglePropertyValue(UNRESOLVED_VALUE))
            .build();

    private static final Property RESOLVED_SINGLE_PROPERTY = Property.builder()
            .name(NAME)
            .value(new SinglePropertyValue(RESOLVED_VALUE))
            .build();

    private static final Property UNRESOLVED_LIST_PROPERTY = Property.builder()
            .name(NAME)
            .value(new ListPropertyValue(List.of(UNRESOLVED_VALUE)))
            .build();

    private static final Property RESOLVED_LIST_PROPERTY = Property.builder()
            .name(NAME)
            .value(new ListPropertyValue(List.of(RESOLVED_VALUE)))
            .build();

    @Mock
    private PropertyPlaceholderResolver resolver;

    private PropertyPlaceholderResolverAggregator aggregator;
    private PropertiesContainer unresolvedPropertiesContainer;

    @BeforeEach
    public void setUp() {
        aggregator = PropertyPlaceholderResolverAggregator.usingResolvers(List.of(resolver));
        when(resolver.resolvePlaceholders(eq(UNRESOLVED_VALUE), any(PropertiesContainer.class)))
                .thenReturn(RESOLVED_VALUE);
    }

    @Test
    public void shouldResolveProperties_whenSingleValuedProperty() {
        unresolvedPropertiesContainer = PropertiesContainerFactory
                .fromProperties(List.of(UNRESOLVED_SINGLE_PROPERTY));

        var resolvedPropertiesContainer = aggregator.resolvePlaceholders(unresolvedPropertiesContainer);

        assertEquals(
                PropertiesContainerFactory.fromProperties(List.of(RESOLVED_SINGLE_PROPERTY)),
                resolvedPropertiesContainer
        );
    }

    @Test
    public void shouldResolveProperties_whenListValuedProperty() {
        unresolvedPropertiesContainer = PropertiesContainerFactory
                .fromProperties(List.of(UNRESOLVED_LIST_PROPERTY));

        var resolvedPropertiesContainer = aggregator.resolvePlaceholders(unresolvedPropertiesContainer);

        assertEquals(
                PropertiesContainerFactory.fromProperties(List.of(RESOLVED_LIST_PROPERTY)),
                resolvedPropertiesContainer
        );
    }
}