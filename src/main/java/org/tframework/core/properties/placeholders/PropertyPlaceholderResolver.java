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

import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.properties.Property;
import org.tframework.core.properties.PropertyValue;

/**
 * The property placeholder resolver is a post processor for properties. It will replace placeholders in the property
 * values with different things, depending on the implementation. This layer does not know about {@link Property} or
 * {@link PropertyValue} objects, it only works with the raw property values.
 */
public interface PropertyPlaceholderResolver {

    /**
     * Resolves placeholders in the given property value.
     * @param propertyValue The property value to resolve the placeholders in.
     * @param propertiesContainer The {@link PropertiesContainer} that contains all properties. Some
     *                            resolvers may need to look up other properties to resolve the placeholders.
     * @return The property with the placeholders resolved.
     */
    String resolvePlaceholders(String propertyValue, PropertiesContainer propertiesContainer);

}
