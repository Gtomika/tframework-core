package org.tframework.profiles;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.profiles.scanners.CLIProfileScanner;
import org.tframework.core.utils.CliUtils;
import org.tframework.test.TframeworkAssertions;
import org.tframework.test.annotations.IsolatedTFrameworkTest;
import org.tframework.test.annotations.SetCommandLineArguments;

import java.util.Set;

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
        TframeworkAssertions.assertHasNonDefaultProfilesExactly(profilesContainer, Set.of("integration-test", "dev"));
    }
}
