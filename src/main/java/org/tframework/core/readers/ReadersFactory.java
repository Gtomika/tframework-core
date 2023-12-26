/* Licensed under Apache-2.0 2023. */
package org.tframework.core.readers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Factory methods for the readers in this package.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReadersFactory {

    /**
     * Creates an {@link EnvironmentVariableReader} to access system variables.
     */
    public static EnvironmentVariableReader createEnvironmentVariableReader() {
        return new EnvironmentVariableReader(System::getenv);
    }

}
