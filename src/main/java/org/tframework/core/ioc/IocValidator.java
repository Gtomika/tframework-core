package org.tframework.core.ioc;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IocValidator {

    private static final Pattern VALID_NAME_PATTERN = Pattern.compile("[a-zA-Z1-9.-]*");

    /**
     * Validates that a name for the managed entity is allowed. Only alphanumeric characters
     * and '.' and '-' characters are allowed.
     * @param name The name to validate.
     * @throws IllegalArgumentException If the name is not valid.
     * @return The name, if it is valid.
     */
    public static String validateEntityName(String name) throws IllegalArgumentException {
        if(!VALID_NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException(name);
        }
        return name;
    }

}
