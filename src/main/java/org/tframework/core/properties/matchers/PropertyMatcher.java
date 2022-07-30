package org.tframework.core.properties.matchers;

import java.util.ArrayList;
import java.util.List;

/**
 * Interface that defines what property matchers must implement. A property matcher
 * takes the property values and determines its type, then converts it to this value.
 *
 * <h2>Registering matchers</h2>
 * Create a class that implements this interface and mark it with {@link org.tframework.core.properties.annotations.PropertyMatcherImplementation}.
 * The set of default matchers can be found under {@code org.tframework.properties.matchers}.
 *
 * <h2>Matcher conditions</h2>
 * To be a valid matcher, these must be respected:
 * <ul>
 *     <li>Implement {@link PropertyMatcher}.</li>
 *     <li>Have a no-args public constructor. It will be used to create an instance of the class.</li>
 *     <li>
 *         Annotate it with {@link org.tframework.core.properties.annotations.PropertyMatcherImplementation}. Not doing so
 *         will not raise an error but the matcher will not be found.
 *     </li>
 * </ul>
 * @param <T> The class that this matcher matches and converts to.
 */
public interface PropertyMatcher<T> {

    /**
     * Checks if the raw property has correct type for this matcher.
     * @param rawProperty The raw property.
     * @return True only if the property matches the type.
     */
    boolean matchesRawProperty(String rawProperty);

    /**
     * Checks if the iterable of raw properties has only elements matching the type of this matcher.
     * Uses {@link #matchesRawProperty(String)} for individual elements.
     * @param rawPropertyIterable {@link Iterable} with the raw properties.
     * @return True if all elements match.
     */
    default boolean matchesRawPropertyIterable(Iterable<String> rawPropertyIterable) {
        for(String rawPropertyElement: rawPropertyIterable) {
            if(!matchesRawProperty(rawPropertyElement)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Parses a raw property into the type used by this matcher. Assumes the raw property
     * matches the type for this matcher (should be checked before with {@link #matchesRawProperty(String)}).
     * @param rawProperty The raw property.
     * @return The parsed property.
     */
    T fromRawProperty(String rawProperty);

    /**
     * Parses all the iterable of raw properties to the type of this matcher.
     * Uses {@link #fromRawProperty(String)} for individual elements. Assumes all elements
     * match the type for this matcher.
     * @param rawPropertyIterable {@link Iterable} with the raw properties.
     * @return List with all parsed elements.
     */
    default List<T> fromRawPropertyIterable(Iterable<String> rawPropertyIterable) {
        List<T> properties = new ArrayList<>();
        for(String rawPropertyElement: rawPropertyIterable) {
            properties.add(fromRawProperty(rawPropertyElement));
        }
        return properties;
    }

    /**
     * @return The class of the property that this matcher matches.
     */
    Class<T> getPropertyType();
}
