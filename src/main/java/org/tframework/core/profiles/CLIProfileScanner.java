/* Licensed under Apache-2.0 2023. */
package org.tframework.core.profiles;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * A {@link ProfileScanner} implementation that looks for a specific command line argument to
 * active profiles. This CLI argument must have the following format:
 * <pre>{@code
 * tframework_profiles=p1,p2,p3
 * }</pre>
 * Where {@code p1,p2,p3} can be replaced with the desired profiles, as a comma separated list. This
 * argument may be provided multiple times.
 */
@Slf4j
public class CLIProfileScanner implements ProfileScanner {

    public static final String PROFILES_CLI_ARGUMENT = "tframework_profiles";
    public static final String SEPARATOR = "=";

    private final String[] args;

    /**
     * Create a CLI profile scanner.
     * @param args Args, as provided in the {@code main} method. Must not be null.
     */
    CLIProfileScanner(@NonNull String[] args) {
        this.args = args;
    }

    @Override
    public Set<String> scan() {
        Set<String> profiles = new HashSet<>();
        for(String arg: args) {
            if(arg.startsWith(PROFILES_CLI_ARGUMENT + SEPARATOR)) {
                log.debug("Found an argument that is a candidate for profile activation: '{}'", arg);
                profiles.addAll(extractProfilesFromArgument(arg));
            }
        }
        log.debug("The '{}' profile scanner will attempt to active the following profiles: {}",
                CLIProfileScanner.class.getName(), profiles);
        return profiles;
    }

    /**
     * @param argument Must start with 'tframework_profiles='.
     */
    private Set<String> extractProfilesFromArgument(String argument) {
        String[] argParts = argument.split(SEPARATOR); //may contain more than one separator...

        StringBuilder profiles = new StringBuilder();
        for(int i = 1; i < argParts.length; i++) {
            profiles.append(argParts[i]);
        }
        return new HashSet<>(Arrays.asList(profiles.toString().split(",")));
    }
}
