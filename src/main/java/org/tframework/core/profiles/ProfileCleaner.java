/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import java.util.Locale;

/**
 * The profile cleaner is responsible for creating a unified format
 * for profiles. This includes:
 * <ul>
 *     <li>Converting letters to lower case.</li>
 *     <li>Stripping leading and trailing whitespace characters.</li>
 * </ul>
 * This class will not validate, raise exceptions or remove any profiles from its input.
 * @see ProfileValidator
 */
public class ProfileCleaner {

    /**
     * Cleans the profile according to the rules specified at the class documentation.
     */
    public String clean(String profile) {
        return profile.toLowerCase(Locale.ROOT).strip();
    }

}
