/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.parsers;

import org.tframework.core.properties.PropertyValue;

/**
 * Property parsers are responsible for converting raw (String) property name-value pairs into
 * {@link ParsedProperty} objects.
 * <ul>
 *     <li>The parser must decide which part of the raw property is the name and the value.</li>
 *     <li>The parser must decide what kind of {@link PropertyValue} it should use.</li>
 * </ul>
 */
public interface PropertyParser {

    /**
     * Converts the raw property string into a {@link ParsedProperty} object.
     * @throws PropertyParsingException If the raw value is invalid.
     */
    ParsedProperty parseProperty(String rawProperty) throws PropertyParsingException;

}
