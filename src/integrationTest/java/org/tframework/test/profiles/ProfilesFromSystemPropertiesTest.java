/*
Copyright 2024 Tamas Gaspar

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
