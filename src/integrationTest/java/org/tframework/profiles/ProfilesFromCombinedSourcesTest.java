/* Licensed under Apache-2.0 2024. */
package org.tframework.profiles;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.profiles.scanners.CLIProfileScanner;
import org.tframework.core.utils.CliUtils;
import org.tframework.test.TframeworkAssertions;
import org.tframework.test.annotations.IsolatedTFrameworkTest;
import org.tframework.test.annotations.SetCommandLineArguments;
import org.tframework.test.annotations.SetProfiles;

@SetProfiles({"p1", "p2"})
@SetCommandLineArguments(ProfilesFromCombinedSourcesTest.PROFILES_SETTER_ARGUMENT)
@IsolatedTFrameworkTest
public class ProfilesFromCombinedSourcesTest {

    public static final String PROFILES_SETTER_ARGUMENT = CLIProfileScanner.PROFILES_CLI_ARGUMENT_KEY +
            CliUtils.CLI_KEY_VALUE_SEPARATOR + "p2,p3";

    @Test
    public void shouldDetectProfilesFromCombinedSources(@InjectElement ProfilesContainer profilesContainer) {
        TframeworkAssertions.assertHasNonDefaultProfilesExactly(profilesContainer, Set.of("p1", "p2", "p3"));
    }
}
