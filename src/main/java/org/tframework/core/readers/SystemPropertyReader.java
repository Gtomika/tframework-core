/* Licensed under Apache-2.0 2024. */
package org.tframework.core.readers;

import java.util.Optional;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Reader for fetching system properties. Basically a wrapper for {@link System#getProperty(String)},
 * except this throws {@link SystemPropertyNotFoundException} instead of returning null when no property was found.
 * This class mainly exists so that code relying on system properties can be tested (by mocking this class).
 * @see ReadersFactory
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class SystemPropertyReader {

    private final Function<String, String> systemPropertyAccessor;

    /**
     * Fetch a property from the system.
     * @param name Property name to fetch.
     * @return The value of the property, if it exists.
     * @throws SystemPropertyNotFoundException If a property by this name does not exist.
     */
    public String readProperty(String name) {
        return Optional.ofNullable(systemPropertyAccessor.apply(name))
                .orElseThrow(() -> new SystemPropertyNotFoundException(name));
    }

}
