/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import java.util.Set;
import lombok.NonNull;

/**
 * Contains the profiles and some related utility methods.
 * @param profiles {@link Set} of profiles.
 */
public record ProfilesContainer(Set<String> profiles) {

    /**
     * Checks if a profile is set.
     * @param profile Non null profile to check: case sensitive.
     */
    public boolean isProfileSet(@NonNull String profile) {
        return profiles.contains(profile);
    }

}
