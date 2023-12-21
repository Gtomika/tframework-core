/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import java.util.Set;

/**
 * This record is the result of the profile initialization process.
 * @param profiles {@link Set} of profiles.
 * @see ProfileInitializationProcess
 */
public record Profiles(Set<String> profiles) {

    /**
     * Checks if a profile is set.
     */
    public boolean hasProfile(String profile) {
        return profiles.contains(profile);
    }

}
