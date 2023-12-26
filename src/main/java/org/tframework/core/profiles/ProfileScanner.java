/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import java.util.Set;

/**
 * A profile scanner is responsible to detect profiles from a single location,
 * which is implementation dependent: can be environmental variables, command line arguments, etc.
 * The scanner implementations should not attempt to clean or validate or process the profiles,
 * these are the responsibilities of other components.
 * @see ProfileScannersFactory
 */
public interface ProfileScanner {

    /**
     * Scan for the profiles.
     * @return A {@link Set} with the detected profiles. This must not be null: of no profiles were found, empty set
     * should be returned.
     */
    Set<String> scan();

}
