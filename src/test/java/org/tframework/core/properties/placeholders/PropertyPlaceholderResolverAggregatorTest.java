/*
Copyright 2024 Tamas Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.tframework.core.properties.placeholders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
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
