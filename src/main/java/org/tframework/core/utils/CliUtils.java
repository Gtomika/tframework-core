/* Licensed under Apache-2.0 2023. */
package org.tframework.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility methods to deal with command line arguments.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CliUtils {

    /**
     * Separator between "key-value" style argument keys and values.
     */
    public static final String CLI_KEY_VALUE_SEPARATOR = "=";

    /**
     * Checks if the CLI argument is in the form of {@code key=value}, where {@code key}
     * and {@code value} are separated by {@link #CLI_KEY_VALUE_SEPARATOR}.
     * @param argument The CLI argument to check.
     * @param key The key to search for in {@code argument}.
     * @return True only if {@code argument} starts with {@code key} + {@link #CLI_KEY_VALUE_SEPARATOR}.
     */
    public static boolean isArgumentWithKey(String argument, String key) {
        return argument.startsWith(key + CLI_KEY_VALUE_SEPARATOR);
    }

    /**
     * Extracts {@code value} from a CLI argument in the form of {@code key=value}, where {@code key}
     * and {@code value} are separated by {@link #CLI_KEY_VALUE_SEPARATOR}. Use {@link #isArgumentWithKey(String, String)}
     * to check if {@code argument} is in this format first.
     * @param argument Argument to extract the value from. Assumed to be in the format described above.
     * @return The extracted key, if argument was in the expected format.
     */
    public static String extractArgumentValue(String argument) {
        //with expected argument, this will not be -1
        int firstSeparatorIndex = argument.indexOf(CLI_KEY_VALUE_SEPARATOR);
        return argument.substring(firstSeparatorIndex + 1);
    }

}
