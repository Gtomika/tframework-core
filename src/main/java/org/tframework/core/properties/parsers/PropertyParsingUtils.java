/* Licensed under Apache-2.0 2024. */
package org.tframework.core.properties.parsers;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Various utility methods to aid {@link PropertyParser}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertyParsingUtils {

    public static final String ESCAPE_CHARACTER = "\\";

    /**
     * The string that separates the name and value parts inside a raw property string.
     */
    public static final String PROPERTY_NAME_VALUE_SEPARATOR = "=";

    private static final String SEPARATOR_REGEX = "(?<!\\" + ESCAPE_CHARACTER + ")" + PROPERTY_NAME_VALUE_SEPARATOR;

    static final String SEPARATOR_NOT_FOUND_ERROR = "The separator '" + PROPERTY_NAME_VALUE_SEPARATOR + "' was not " +
            "found in the raw property string. Cannot determine what is property name and what is the value";

    static final String MULTIPLE_SEPARATOR_FOUND_ERROR = "The separator '" + PROPERTY_NAME_VALUE_SEPARATOR + "' was found " +
            "multiple times. Escape with '" + ESCAPE_CHARACTER + "' so that only one separator remains.";

    /**
     * Converts a raw property string into name and value.
     * <ul>
     *     <li>{@code rawProperty} must contain exactly one non-escaped {@value PROPERTY_NAME_VALUE_SEPARATOR}.</li>
     *     <li>
     *         If the {@value PROPERTY_NAME_VALUE_SEPARATOR} is to be used as part of the name or value, it must be
     *         escaped as {@value ESCAPE_CHARACTER}{@value PROPERTY_NAME_VALUE_SEPARATOR}.
     *     </li>
     * </ul>
     * @param rawProperty Property to separate, must not be null.
     * @return {@link SeparatedProperty} object with the separated data.
     * @throws PropertyParsingException If one of the rules described above is violated.
     */
    public static SeparatedProperty separateNameValue(@NonNull String rawProperty) throws PropertyParsingException {
        String[] parts = rawProperty.split(SEPARATOR_REGEX);
        if(parts.length < 2) {
            throw new PropertyParsingException(rawProperty, SEPARATOR_NOT_FOUND_ERROR);
        }
        if(parts.length > 2) {
            throw new PropertyParsingException(rawProperty, MULTIPLE_SEPARATOR_FOUND_ERROR);
        }
        return new SeparatedProperty(parts[0], parts[1]);
    }

    public static final String LIST_BEGIN_CHARACTER = "[";
    public static final String LIST_END_CHARACTER = "]";

    private static final Pattern LIST_PATTERN = Pattern.compile(
            "^(?<!\\" + ESCAPE_CHARACTER + ")\\" + LIST_BEGIN_CHARACTER + ".*" + "(?<!\\" + ESCAPE_CHARACTER + ")\\" + LIST_END_CHARACTER
    );

    /**
     * Determines if a raw property value is list. It is only a list if:
     * <ul>
     *     <li>Begins with {@value LIST_BEGIN_CHARACTER} and ends with {@value LIST_END_CHARACTER}.</li>
     *     <li>Neither of these begin and end characters are escaped using {@value ESCAPE_CHARACTER}.</li>
     * </ul>
     * @param rawPropertyValue Raw property value to check, must not be null.
     * @return True only if the raw value is a list according to the rules above.
     */
    public static boolean isListValue(@NonNull String rawPropertyValue) {
        return LIST_PATTERN.matcher(rawPropertyValue).matches();
    }

    public static final String LIST_ELEMENT_SEPARATOR_CHARACTER = ",";

    private static final String LIST_ELEMENT_SEPARATOR_REGEX = "(?<!\\" + ESCAPE_CHARACTER + ")" + LIST_ELEMENT_SEPARATOR_CHARACTER;

    /**
     * Extracts list elements from a raw property value. It is assumed that this raw value passes the
     * {@link #isListValue(String)} check first.
     * <ul>
     *     <li>List elements are inside the {@value LIST_BEGIN_CHARACTER} ... {@value LIST_END_CHARACTER}.</li>
     *     <li>The only way to create an empty list is to use {@value LIST_BEGIN_CHARACTER}{@value LIST_END_CHARACTER}.</li>
     *     <li>List elements are separated by {@value LIST_ELEMENT_SEPARATOR_CHARACTER}.</li>
     *     <li>Whitespaces are included into the elements.</li>
     *     <li>{@value LIST_ELEMENT_SEPARATOR_CHARACTER} can be escaped with {@value ESCAPE_CHARACTER} to be treated as part of an element.</li>
     * </ul>
     * @param rawPropertyValue Value to extract list elements from. Must not be null. Must pass {@link #isListValue(String)}.
     * @return
     */
    public static List<String> extractListElements(@NonNull String rawPropertyValue) {
        //removing [ and ]
        String strippedList = rawPropertyValue.substring(1, rawPropertyValue.length()-1);

        var elements = Arrays.stream(strippedList.split(LIST_ELEMENT_SEPARATOR_REGEX, -1)) //-1 makes sure empty values are kept
                //the escaped element separator and replaced with simple separators
                .map(element -> element.replaceAll("\\" + ESCAPE_CHARACTER + LIST_ELEMENT_SEPARATOR_CHARACTER, LIST_ELEMENT_SEPARATOR_CHARACTER))
                .toList();

        if(elements.size() == 1 && "".equals(elements.get(0))) {
            //this is a special case when we need to create an empty list instead
            //of a list with the "" element
            return List.of();
        } else {
            return elements;
        }
    }
}
