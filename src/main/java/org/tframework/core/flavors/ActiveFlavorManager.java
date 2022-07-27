package org.tframework.core.flavors;

import lombok.extern.slf4j.Slf4j;
import org.tframework.core.ApplicationContext;
import org.tframework.core.flavors.exceptions.FlavorException;
import org.tframework.core.ioc.annotations.ManagePreConstructedSingleton;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Detects the active flavors on startup and stores them for runtime.
 */
@Slf4j
@ManagePreConstructedSingleton
public class ActiveFlavorManager {

    /**
     * Name of the environmental variable holding the active profiles.
     */
    public static final String ACTIVE_FLAVOR_VAR_NAME = "TFRAMEWORK_CORE_ACTIVE_FLAVORS";

    /**
     * Regex that matches only valid flavor names which contains only lowercase english letters
     * and underscores.
     */
    private static final Pattern FLAVOR_NAME_REGEX = Pattern.compile("[a-z_]+");

    /**
     * Do not use this method, it exists only to fulfill requirements of {@link ManagePreConstructedSingleton}.
     */
    private static ActiveFlavorManager getInstance() {
        return ApplicationContext.getInstance().getActiveFlavorManager();
    }

    /**
     * Stories all active flavors.
     */
    private final Set<String> activeFlavors;

    /**
     * Create an initial {@link ActiveFlavorManager}. It will read that active flavors.
     */
    public ActiveFlavorManager() {
        activeFlavors = new HashSet<>();
        readActiveFlavors();
    }

    /**
     * On startup, finds the active flavors using the supported ways to set them,
     * such as the {@code TFRAMEWORK_ACTIVE_FLAVOR} environmental variable.
     */
    private void readActiveFlavors() {
        activeFlavors.addAll(readActiveFlavorFromEnvironmentalVariable());
        log.info("The following flavors will be active: {}", activeFlavors);
    }

    /**
     * Reads the active flavor list from the {@code TFRAMEWORK_ACTIVE_FLAVOR} environmental variable,
     * which contains the flavors in a comma separated list.
     * @return A set with the active flavors.
     * @throws FlavorException If one flavor has an illegal name.
     */
    private Set<String> readActiveFlavorFromEnvironmentalVariable() throws FlavorException {
        String flavorsFromEnvVar = System.getenv(ACTIVE_FLAVOR_VAR_NAME);
        log.debug("Value of '{}' environmental variable is: {}", ACTIVE_FLAVOR_VAR_NAME, flavorsFromEnvVar);
        if(flavorsFromEnvVar == null) {
            return Set.of();
        }
        Set<String> activeFlavors = Arrays.stream(flavorsFromEnvVar.split(","))
                .collect(Collectors.toSet());
        activeFlavors.forEach(flavor -> {
            if(!isValidFlavorName(flavor)) {
                log.error("Invalid flavor name '{}'", flavor);
                throw new FlavorException(String.format("The flavor name '%s' is invalid. It must contain only english lower case letters " +
                        "and underscores.", flavor));
            }
        });
        return activeFlavors;
    }

    /**
     * Checks if a flavor name is allowed.
     */
    private boolean isValidFlavorName(String flavor) {
        return FLAVOR_NAME_REGEX.matcher(flavor).matches();
    }

    /**
     * Returns a set of the active flavors. Changing the elements of this set does not
     * change the active flavors.
     */
    public Set<String> getActiveFlavors() {
        return Set.copyOf(activeFlavors);
    }

    /**
     * Checks if a flavor is active.
     */
    public boolean isActiveFlavor(String flavor) {
        return activeFlavors.contains(flavor);
    }
}
