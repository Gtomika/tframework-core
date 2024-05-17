/* Licensed under Apache-2.0 2024. */
package org.tframework.test.profiles;

import java.util.Set;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.profiles.scanners.SystemPropertyProfileScanner;
import org.tframework.test.commons.annotations.BeforeFrameworkInitialization;
import org.tframework.test.commons.utils.SystemPropertyHelper;
import org.tframework.test.commons.utils.TframeworkAssertions;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@BeforeFrameworkInitialization(callback = ProfilesFromSystemPropertiesTest.SystemPropertySetter.class)
@IsolatedTFrameworkTest
public class ProfilesFromSystemPropertiesTest {

    private static final SystemPropertyHelper systemPropertyHelper = new SystemPropertyHelper();

    public static class SystemPropertySetter implements Runnable {

        @Override
        public void run() {
            systemPropertyHelper.setIntoSystemProperties(
                    SystemPropertyProfileScanner.PROFILES_SYSTEM_PROPERTY,
                    "integration-test,dev"
            );
        }
    }

    @Test
    public void shouldDetectProfilesFromSystemProperties(@InjectElement ProfilesContainer profilesContainer) {
        TframeworkAssertions.assertHasNonDefaultProfiles(profilesContainer, Set.of("integration-test", "dev"));
    }

    @AfterAll
    public static void afterAll() {
        systemPropertyHelper.cleanUp();
    }
}
