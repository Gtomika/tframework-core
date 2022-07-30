package org.tframework.core.properties.matchers;

import org.tframework.core.properties.annotations.PropertyMatcherImplementation;

import java.util.regex.Pattern;

/**
 * Property matcher that is for integers.
 */
@PropertyMatcherImplementation
public class IntegerMatcher implements PropertyMatcher<Integer> {

    Pattern INT_REGEX = Pattern.compile("-?(0|[1-9]\\d*)");

    @Override
    public boolean matchesRawProperty(String rawProperty) {
        return INT_REGEX.matcher(rawProperty).matches();
    }

    @Override
    public Integer fromRawProperty(String rawProperty) {
        return Integer.parseInt(rawProperty);
    }

    @Override
    public Class<Integer> getPropertyType() {
        return Integer.class;
    }
}
