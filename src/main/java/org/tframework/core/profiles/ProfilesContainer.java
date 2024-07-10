/*
Copyright 2023 Tamas Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
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
