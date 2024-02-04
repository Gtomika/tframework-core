/* Licensed under Apache-2.0 2024. */
package org.tframework.core.readers;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
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
    private final Supplier<Set<String>> systemPropertySupplier;

    SystemPropertyReader() {
        systemPropertyAccessor = System::getProperty;
        systemPropertySupplier = () -> System.getProperties().keySet()
                .stream()
                .map(Object::toString)
                .collect(Collectors.toSet());
    }

    /**
     * Fetch a property from the system.
     * @param name Property name to fetch.
     * @return The value of the property, if it exists.
     * @throws SystemPropertyNotFoundException If a property by this name does not exist.
     */
    public String readSystemProperty(String name) {
        return Optional.ofNullable(systemPropertyAccessor.apply(name))
                .orElseThrow(() -> new SystemPropertyNotFoundException(name));
    }

    /**
     * Gets the names of all system properties available.
     */
    public Set<String> getAllSystemPropertyNames() {
        return systemPropertySupplier.get();
    }

}
