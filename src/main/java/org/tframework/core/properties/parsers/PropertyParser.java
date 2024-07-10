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
package org.tframework.core.properties.parsers;

import org.tframework.core.properties.Property;
import org.tframework.core.properties.PropertyValue;

/**
 * Property parsers are responsible for converting raw (String) property name-value pairs into
 * {@link Property} objects.
 * <ul>
 *     <li>The parser must decide which part of the raw property is the name and the value.</li>
 *     <li>The parser must decide what kind of {@link PropertyValue} it should use.</li>
 * </ul>
 * @see PropertyParsersFactory
 */
public interface PropertyParser {

    /**
     * Converts the raw property string into a {@link Property} object.
     * @throws PropertyParsingException If the raw value is invalid.
     */
    Property parseProperty(String rawProperty) throws PropertyParsingException;

}
