/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import java.util.Set;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.tframework.core.elements.annotations.PreConstructedElement;

/**
 * Read only container of the profiles, and some related utility methods.
 */
@PreConstructedElement
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProfilesContainer {

    private final Set<String> profiles;

    /**
     * Checks if a profile is set.
     * @param profile Non null profile to check: case sensitive.
     */
    public boolean isProfileSet(@NonNull String profile) {
        return profiles.contains(profile);
    }

    /**
     * Creates a copy of the profiles.
     */
    public Set<String> profiles() {
        return Set.copyOf(profiles);
    }

    /**
     * Creates a {@link ProfilesContainer} that will have the given {@link Set} of profiles.
     */
    public static ProfilesContainer fromProfiles(@NonNull Set<String> profiles) {
        return new ProfilesContainer(profiles);
    }

    /**
     * Creates an empty {@link ProfilesContainer}.
     */
    public static ProfilesContainer empty() {
        return new ProfilesContainer(Set.of());
    }

}
