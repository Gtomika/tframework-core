/* Licensed under Apache-2.0 2024. */
package org.tframework.profiles;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.test.commons.annotations.SetProfiles;
import org.tframework.test.commons.utils.TframeworkAssertions;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@SetProfiles({"integration-test", "dev"}) //uses system properties to set profiles
@IsolatedTFrameworkTest
public class ProfilesFromSystemPropertiesTest {

    @Test
    public void shouldDetectProfilesFromSystemProperties(@InjectElement ProfilesContainer profilesContainer) {
        TframeworkAssertions.assertHasNonDefaultProfiles(profilesContainer, Set.of("integration-test", "dev"));
    }

}
