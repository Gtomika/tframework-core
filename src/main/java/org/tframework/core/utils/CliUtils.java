/*
Copyright 2023 Tamas Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
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
