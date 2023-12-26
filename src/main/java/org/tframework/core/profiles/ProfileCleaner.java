/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import java.util.Locale;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The profile cleaner is responsible for creating a unified format
 * for profiles. This includes:
 * <ul>
 *     <li>Converting letters to lower case.</li>
 *     <li>Stripping leading and trailing whitespace characters.</li>
 * </ul>
 * This class will not validate, raise exceptions or remove any profiles from its input.
 */
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ProfileCleaner {

    /**
     * Cleans the profile according to the rules specified at the class documentation.
     */
    public String clean(String profile) {
        return profile.toLowerCase(Locale.ROOT).strip();
    }

}
