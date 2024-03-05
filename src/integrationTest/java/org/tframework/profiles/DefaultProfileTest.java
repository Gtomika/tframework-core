/* Licensed under Apache-2.0 2024. */
package org.tframework.profiles;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.test.commons.utils.TframeworkAssertions;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@IsolatedTFrameworkTest
public class DefaultProfileTest {

    @Test
    public void shouldDetectDefaultProfile(@InjectElement ProfilesContainer profilesContainer) {
        TframeworkAssertions.assertHasNonDefaultProfiles(profilesContainer, Set.of());
    }

}
