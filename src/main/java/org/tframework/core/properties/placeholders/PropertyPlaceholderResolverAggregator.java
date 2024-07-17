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

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.tframework.core.properties.ListPropertyValue;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.properties.PropertiesContainerFactory;
import org.tframework.core.properties.Property;
import org.tframework.core.properties.SinglePropertyValue;

/**
 * Combines several {@link PropertyPlaceholderResolver}s and applies them to a
 * {@link PropertiesContainer}, resolving all placeholders.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PropertyPlaceholderResolverAggregator {

    private final List<PropertyPlaceholderResolver> resolvers;

    /**
     * Applies all resolvers to the given {@link PropertiesContainer}, resolving all placeholders.
     * @param propertiesContainer The {@link PropertiesContainer} to resolve the placeholders in.
     * @return A new {@link PropertiesContainer} with all placeholders resolved.
     */
    public PropertiesContainer resolvePlaceholders(PropertiesContainer propertiesContainer) {
        var resolvedProperties = propertiesContainer.properties().stream()
                .map(property -> resolvePlaceholders(property, propertiesContainer))
                .toList();
        return PropertiesContainerFactory.fromProperties(resolvedProperties);
    }

    private Property resolvePlaceholders(Property property, PropertiesContainer propertiesContainer) {
        return switch (property.value()) {
            case SinglePropertyValue spv -> {
                var resolvedValue = applyAllResolvers(spv.value(), propertiesContainer);
                yield property.withValue(new SinglePropertyValue(resolvedValue));
            }
            case ListPropertyValue lpv -> {
                var resolvedValues = lpv.values().stream()
                        .map(value -> applyAllResolvers(value, propertiesContainer))
                        .toList();
                yield property.withValue(new ListPropertyValue(resolvedValues));
            }
        };
    }

    private String applyAllResolvers(String rawPropertyValue, PropertiesContainer propertiesContainer) {
        var resolvedPropertyValue = rawPropertyValue;
        for (var resolver : resolvers) {
            resolvedPropertyValue = resolver.resolvePlaceholders(resolvedPropertyValue, propertiesContainer);
        }
        return resolvedPropertyValue;
    }

    /**
     * Creates a new {@link PropertyPlaceholderResolverAggregator} with the given resolvers.
     */
    public static PropertyPlaceholderResolverAggregator usingResolvers(List<PropertyPlaceholderResolver> resolvers) {
        return new PropertyPlaceholderResolverAggregator(resolvers);
    }
}
