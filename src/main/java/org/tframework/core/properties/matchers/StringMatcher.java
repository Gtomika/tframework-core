/* Licensed under Apache-2.0 2022. */
package org.tframework.core.properties.matchers;

/**
 * This is the default property matcher, the one that will be tried after
 * all other matchers have failed. It will interpret the raw value as a string.
 * <p>
 * This class is intentionally not annotated with {@link org.tframework.core.properties.annotations.PropertyMatcherImplementation},
 * because it must be placed at the end of the list of matchers.
 */
public class StringMatcher implements PropertyMatcher<String> {

    @Override
    public boolean matchesRawProperty(String rawProperty) {
        return true;
    }

    @Override
    public String fromRawProperty(String rawProperty) {
        return rawProperty;
    }

    @Override
    public Class<String> getPropertyType() {
        return String.class;
    }
}
