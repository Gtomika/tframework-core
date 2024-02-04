/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.scanners;


import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.properties.parsers.PropertyParsingUtils;
import org.tframework.core.readers.SystemPropertyReader;

/**
 * A {@link PropertyScanner} that finds <b>framework</b> properties to set in the <b>system</b> properties.
 * For a system property to be considered, its name must start with {@value PROPERTY_PREFIX}. Then, the name
 * must follow with the property name. The value of the system property should be the value of the framework property.
 * For example, to set a framework property {@code some.cool.prop} to value {@code 123} using this way, we need the
 * following system property:
 * <pre>{@code
 * tframework.property.some.cool.prop
 * }</pre>
 * And to this system property, we must assign the value {@code 123}.
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class SystemPropertyScanner implements PropertyScanner {

    public static final String PROPERTY_PREFIX = "tframework.property.";

    private final SystemPropertyReader systemPropertyReader;

    @Override
    public List<String> scanProperties() {
        return systemPropertyReader.getAllSystemPropertyNames().stream()
                .filter(systemProp -> systemProp.startsWith(PROPERTY_PREFIX))
                .peek(systemProp -> log.debug("Found system property '{}' that is candidate for property scanning", systemProp))
                .map(systemProp -> {
                    String propertyName = systemProp.replaceFirst(PROPERTY_PREFIX, "");
                    String propertyValue = systemPropertyReader.readSystemProperty(systemProp);
                    return propertyName + PropertyParsingUtils.PROPERTY_NAME_VALUE_SEPARATOR + propertyValue;
                })
                .peek(rawProperty -> log.debug("Found '{}' raw property in system properties", rawProperty))
                .toList();
    }
}
