/* Licensed under Apache-2.0 2023. */
package org.tframework.core.readers;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * Reader for fetching environment variables. Basically a wrapper for {@link System#getenv()},
 * except this throws {@link EnvironmentVariableNotFoundException} instead of returning null when no variable was found.
 * This class mainly exists so that code relying on environmental variables can be tested (by mocking this class).
 * @see ReadersFactory
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class EnvironmentVariableReader {

    private final Function<String, String> variableAccessor;
    private final Supplier<Set<String>> variablesIterator;

    EnvironmentVariableReader() {
        variableAccessor = System::getenv;
        variablesIterator = () -> System.getenv().keySet();
    }

    /**
     * Fetch a variable from the environment.
     * @param name Variable name to fetch.
     * @return The value of the variable, if it exists.
     * @throws EnvironmentVariableNotFoundException If a variable by this name does not exist.
     */
    public String readVariable(String name) {
        return Optional.ofNullable(variableAccessor.apply(name))
                .orElseThrow(() -> new EnvironmentVariableNotFoundException(name));
    }

    /**
     * Gets all available variable names from the environment.
     */
    public Set<String> getAllVariableNames() {
        return variablesIterator.get();
    }

}
