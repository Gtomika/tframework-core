package org.tframework.core.properties;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.tframework.core.properties.exceptions.PropertyException;
import org.tframework.core.properties.exceptions.PropertyMatcherException;
import org.tframework.core.properties.matchers.PropertyMatcher;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Utility methods for property related validations.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PropertyValidator {

    /**
     * Validates a class annotated with {@link org.tframework.core.properties.annotations.PropertyMatcherImplementation}.
     * It is valid only if:
     * <ul>
     *     <li>Annotated with {@link org.tframework.core.properties.annotations.PropertyMatcherImplementation}. This is assumed.</li>
     *     <li>Implements {@link org.tframework.core.properties.matchers.PropertyMatcher}.</li>
     *     <li>Has a no-args public constructor.</li>
     * </ul>
     * @param clazz The class, assumed to be annotated with {@link org.tframework.core.properties.annotations.PropertyMatcherImplementation}.
     * @throws PropertyMatcherException If the class is invalid.
     */
    public static void validatePropertyMatcher(Class<?> clazz) throws PropertyMatcherException {
        if(!Arrays.asList(clazz.getInterfaces()).contains(PropertyMatcher.class)) {
            throw new PropertyMatcherException(String.format("The property matcher '%s' is not implementing '%s'.",
                    clazz.getName(), PropertyMatcher.class.getName()));
        }
        if(ConstructorUtils.getMatchingAccessibleConstructor(clazz) == null) {
            throw new PropertyMatcherException(String.format("The property matcher '%s' has no public no-args constructor.", clazz.getName()));
        }
    }

    private static final Pattern NAME_SECTION_REGEX = Pattern.compile("[a-z_]+");

    /**
     * Validates a property name. It is valid if it has only these characters:
     * <ul>
     *     <li>Lowercase english letters.</li>
     *     <li>Underscores.</li>
     *     <li>Dots, separating sequences of the other characters above. These sequences cannot be empty.</li>
     * </ul>
     * For example these are valid:
     * <ul>
     *     <li>{@code my.property}</li>
     *     <li>{@code com.something.useful_property}</li>
     *     <li>{@code good_stuff}</li>
     * </ul>
     * But these are not valid:
     * <ul>
     *     <li>{@code my.1st.property}</li>
     *     <li>{@code my.property.}</li>
     *     <li>{@code my..property}</li>
     * </ul>
     * @throws PropertyException If the name is invalid.
     */
    public static void validatePropertyName(String name) throws PropertyException {
        if(name == null || name.isBlank()) {
            throw new PropertyException(name, "Property names cannot be empty");
        }
        String[] nameSections = name.split("\\.");
        for(String section: nameSections) {
            if(!NAME_SECTION_REGEX.matcher(section).matches()) {
                throw new PropertyException(name, String.format("The name segment '%s' is either empty or contains invalid characters. " +
                        "Only lowercase english letters and underscores are allowed between the dots.", section));
            }
        }
    }

}
