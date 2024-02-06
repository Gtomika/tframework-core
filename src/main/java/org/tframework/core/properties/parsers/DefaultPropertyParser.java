/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.parsers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.properties.ListPropertyValue;
import org.tframework.core.properties.Property;
import org.tframework.core.properties.PropertyValue;
import org.tframework.core.properties.SinglePropertyValue;

/**
 * A default implementation of {@link PropertyParser} that is used by the framework to
 * parse raw property strings.
 * <ul>
 *     <li>Raw property values must contain the separator. See {@link PropertyParsingUtils#separateNameValue(String)}.</li>
 *     <li>The raw value is checked if it is a list or not. See {@link PropertyParsingUtils#isListValue(String)}.</li>
 *     <li>If list, the elements are extracted. See {@link PropertyParsingUtils#extractListElements(String)}.</li>
 *     <li>If single value, it is returned as is.</li>
 * </ul>
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class DefaultPropertyParser implements PropertyParser {

    static final String BLANK_NAME_ERROR = "The name of a property cannot be blank.";

    @Override
    public Property parseProperty(String rawProperty) {
        var separatedProperty = PropertyParsingUtils.separateNameValue(rawProperty);
        log.trace("Separated raw property '{}' into name: '{}', value: '{}'", rawProperty, separatedProperty.name(), separatedProperty.value());

        if(separatedProperty.name().isBlank()) {
            throw new PropertyParsingException(rawProperty, BLANK_NAME_ERROR);
        }

        if(PropertyParsingUtils.isListValue(separatedProperty.value())) {
            log.trace("Raw property '{}' is a list.", rawProperty);

            var elements = PropertyParsingUtils.extractListElements(separatedProperty.value());
            log.trace("Raw property '{}' has following list elements: {}", rawProperty, elements);

            PropertyValue propertyValue = new ListPropertyValue(elements);
            return new Property(separatedProperty.name(), propertyValue);
        } else {
            log.trace("Raw property '{}' has a single value", rawProperty);
            PropertyValue propertyValue = new SinglePropertyValue(separatedProperty.value());
            return new Property(separatedProperty.name(), propertyValue);
        }
    }
}
