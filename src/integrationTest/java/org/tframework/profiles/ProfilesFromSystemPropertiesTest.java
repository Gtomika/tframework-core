/* Licensed under Apache-2.0 2024. */
package org.tframework.profiles;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.test.TframeworkAssertions;
import org.tframework.test.annotations.IsolatedTFrameworkTest;
import org.tframework.test.annotations.SetProfiles;

@SetProfiles({"integration-test", "dev"}) //uses system properties to set profiles
@IsolatedTFrameworkTest
public class ProfilesFromSystemPropertiesTest {

    @Test
    public void shouldDetectProfilesFromSystemProperties(@InjectElement ProfilesContainer profilesContainer) {
        TframeworkAssertions.assertHasNonDefaultProfilesExactly(profilesContainer, Set.of("integration-test", "dev"));
    }

}
