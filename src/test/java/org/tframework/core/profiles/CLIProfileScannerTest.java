/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.tframework.core.profiles.CLIProfileScanner.PROFILES_CLI_ARGUMENT;
import static org.tframework.core.profiles.CLIProfileScanner.SEPARATOR;

import java.util.Set;
import org.junit.jupiter.api.Test;

class CLIProfileScannerTest {

    @Test
    public void shouldFindProfile_ifProvided() {
        String[] args = {PROFILES_CLI_ARGUMENT + SEPARATOR + "dev,db", "otherArgXX"};
        var cliProfileScanner = new CLIProfileScanner(args);

        var profiles = cliProfileScanner.scan();
        assertEquals(Set.of("dev", "db"), profiles);
    }

    @Test
    public void shouldFindProfile_ifProvided_asMultipleArgs() {
        String[] args = {PROFILES_CLI_ARGUMENT + SEPARATOR + "dev,db", "otherArgXX", PROFILES_CLI_ARGUMENT + SEPARATOR + "test"};
        var cliProfileScanner = new CLIProfileScanner(args);

        var profiles = cliProfileScanner.scan();
        assertEquals(Set.of("dev", "db", "test"), profiles);
    }

    @Test
    public void shouldFindProfile_asEmptySet_ifArgsContainsNoProfiles() {
        String[] args = {"otherArgXX"};
        var cliProfileScanner = new CLIProfileScanner(args);

        var profiles = cliProfileScanner.scan();
        assertTrue(profiles.isEmpty());
    }

}
