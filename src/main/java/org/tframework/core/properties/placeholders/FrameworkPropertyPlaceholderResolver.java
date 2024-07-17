package org.tframework.core.properties.placeholders;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.properties.ListPropertyValue;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.properties.PropertyNotFoundException;
import org.tframework.core.properties.SinglePropertyValue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This {@link PropertyPlaceholderResolver} resolves values from the frameworks properties. Basically it
 * allows to embed properties into each other. It will find and replace substrings in the value that are in the
 * format of {@code prop{PROP_NAME}} and replace them with the value of the property {@code PROP_NAME}. It is
 * possible to specify default value using {@code prop{PROP_NAME|default_value}}. Notes:
 * <ul>
 *     <li>
 *        Note that the property that will be resolved <b>will not be resolved recursively</b>. If the property
 *        value contains another property placeholder, that will not be resolved.
 *     </li>
 *     <li>
 *         If the resolved property is a list, it will be converted to a string by joining the elements with a comma.
 *     </li>
 * </ul>
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class FrameworkPropertyPlaceholderResolver implements PropertyPlaceholderResolver {

    private static final Pattern PROP_PATTERN = Pattern.compile("prop\\{([^}|]+)(\\|([^}|]+))?}");

    @Override
    public String resolvePlaceholders(String propertyValue, PropertiesContainer propertiesContainer) {
        Matcher matcher = PROP_PATTERN.matcher(propertyValue);
        StringBuilder resolvedValue = new StringBuilder();

        while(matcher.find()) {
            String propName = matcher.group(1);
            String propDefaultValue = matcher.group(3); // This will be null if the default value is not specified
            log.debug("Found framework property placeholder in property value '{}'. Property: {}, default value: {}",
                    propertyValue, propName, propDefaultValue);

            String resolvedProp = resolveProperty(propName, propDefaultValue, propertiesContainer);
            log.debug("Resolved property '{}' placeholder to '{}'", propName, resolvedProp);
            matcher.appendReplacement(resolvedValue, resolvedProp);
        }

        matcher.appendTail(resolvedValue);
        return resolvedValue.toString();
    }

    private String resolveProperty(String propName, String defaultValue, PropertiesContainer propertiesContainer) {
        try {
            var resolvedValue = propertiesContainer.getPropertyValueObject(propName);
            return switch (resolvedValue) {
                case SinglePropertyValue spv -> spv.value();
                case ListPropertyValue lpv -> String.join(",", lpv.values());
            };
        } catch (PropertyNotFoundException e) {
            if(defaultValue != null) {
                log.debug("Property {} not found, using default value: {}", propName, defaultValue);
                return defaultValue;
            } else {
                throw e;
            }
        }
    }
}
