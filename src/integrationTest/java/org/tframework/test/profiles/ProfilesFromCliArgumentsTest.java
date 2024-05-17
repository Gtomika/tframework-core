/* Licensed under Apache-2.0 2024. */
package org.tframework.test.profiles;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.profiles.scanners.CLIProfileScanner;
import org.tframework.core.utils.CliUtils;
import org.tframework.test.commons.annotations.SetCommandLineArguments;
import org.tframework.test.commons.utils.TframeworkAssertions;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@SetCommandLineArguments({
        ProfilesFromCliArgumentsTest.PROFILES_SETTER_ARGUMENT,
        "some.other.argument=irrelevant"
})
@IsolatedTFrameworkTest
public class ProfilesFromCliArgumentsTest {

    public static final String PROFILES_SETTER_ARGUMENT = CLIProfileScanner.PROFILES_CLI_ARGUMENT_KEY +
            CliUtils.CLI_KEY_VALUE_SEPARATOR + "integration-test,dev";

    @Test
    public void shouldDetectProfilesFromCliArguments(@InjectElement ProfilesContainer profilesContainer) {
        TframeworkAssertions.assertHasNonDefaultProfiles(profilesContainer, Set.of("integration-test", "dev"));
    }
}
