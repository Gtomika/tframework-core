package org.tframework.profiles;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.test.TframeworkAssertions;
import org.tframework.test.annotations.IsolatedTFrameworkTest;

import java.util.Set;

@IsolatedTFrameworkTest
public class DefaultProfileTest {

    @Test
    public void shouldDetectDefaultProfile(@InjectElement ProfilesContainer profilesContainer) {
        TframeworkAssertions.assertHasNonDefaultProfilesExactly(profilesContainer, Set.of());
    }

}
