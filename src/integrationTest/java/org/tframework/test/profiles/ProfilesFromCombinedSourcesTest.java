/* Licensed under Apache-2.0 2024. */
package org.tframework.test.profiles;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.profiles.scanners.CLIProfileScanner;
import org.tframework.core.utils.CliUtils;
import org.tframework.test.commons.annotations.SetCommandLineArguments;
import org.tframework.test.commons.annotations.SetProfiles;
import org.tframework.test.commons.utils.TframeworkAssertions;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@SetProfiles({"p1", "p2"})
@SetCommandLineArguments(ProfilesFromCombinedSourcesTest.PROFILES_SETTER_ARGUMENT)
@IsolatedTFrameworkTest
public class ProfilesFromCombinedSourcesTest {

    public static final String PROFILES_SETTER_ARGUMENT = CLIProfileScanner.PROFILES_CLI_ARGUMENT_KEY +
            CliUtils.CLI_KEY_VALUE_SEPARATOR + "p2,p3";

    @Test
    public void shouldDetectProfilesFromCombinedSources(@InjectElement ProfilesContainer profilesContainer) {
        TframeworkAssertions.assertHasNonDefaultProfiles(profilesContainer, Set.of("p1", "p2", "p3"));
    }
}
