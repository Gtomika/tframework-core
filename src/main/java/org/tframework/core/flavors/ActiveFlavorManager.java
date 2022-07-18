package org.tframework.core.flavors;

import lombok.extern.slf4j.Slf4j;
import org.tframework.core.ioc.annotations.Managed;

import java.util.HashSet;
import java.util.Set;

/**
 * Detects the active flavors on startup and stores them for runtime.
 */
@Managed
@Slf4j
public class ActiveFlavorManager {

    /**
     * Stories all active flavors.
     */
    private final Set<String> activeFlavors;

    /**
     * Create an initial {@link ActiveFlavorManager}.
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

    }

    /**
     * Reads the active flavor list from the {@code TFRAMEWORK_ACTIVE_FLAVOR} environmental variable,
     * which contains the flavors in a comma separated list.
     * @return A set with the active flavors.
     */
    private Set<String> readActiveFlavorFromEnvironmentalVariable() {
        return null;
    }

    private Set<String> readActiveFlavorsFromProperty() {
        return null;
    }

}
