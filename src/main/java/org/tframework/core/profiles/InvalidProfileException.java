/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import java.util.Set;
import lombok.NonNull;
import org.tframework.core.TFrameworkException;

/**
 * This exception is thrown when a profile name does not match framework rules.
 * These rules are documented in {@link ProfileValidator}.
 * @see ProfileValidator
 */
public class InvalidProfileException extends TFrameworkException {

    private static final String TEMPLATE = "The profiles '%s' have invalid name!" +
            " Check '%s' documentation for the rules.";

    public InvalidProfileException(@NonNull String profile) {
        super(TEMPLATE.formatted(profile, ProfileValidator.class.getName()));
    }

    public InvalidProfileException(@NonNull Set<String> profiles) {
        super(TEMPLATE.formatted(profiles, ProfileValidator.class.getName()));
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
