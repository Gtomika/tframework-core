/* Licensed under Apache-2.0 2023. */
package org.tframework.core.utils;

import java.util.Optional;
import java.util.function.Function;

/**
 * Reader for fetching environment variables. Basically a wrapper for {@link System#getenv()},
 * except this throws {@link EnvironmentVariableNotFoundException} instead of returning null when no variable was found.
 * This class mainly exists so that code relying on environmental variables can be tested (by mocking this class).
 */
public class EnvironmentVariableReader {

    private final Function<String, String> variableExtractor;

    /**
     * Creates a variable reader.
     */
    public EnvironmentVariableReader() {
        variableExtractor = System::getenv;
    }

    //for unit testing
    protected EnvironmentVariableReader(Function<String, String> variableExtractor) {
        this.variableExtractor = variableExtractor;
    }

    /**
     * Fetch a variable from the environment.
     * @param name Variable name to fetch.
     * @return The value of the variable, if it exists.
     * @throws EnvironmentVariableNotFoundException If a variable by this name does not exist.
     */
    public String readVariable(String name) {
        return Optional.ofNullable(variableExtractor.apply(name))
                .orElseThrow(() -> new EnvironmentVariableNotFoundException(name));
    }

}
