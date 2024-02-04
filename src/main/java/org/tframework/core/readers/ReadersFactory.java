/* Licensed under Apache-2.0 2023. */
package org.tframework.core.readers;

import java.util.function.Function;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.utils.ClassLoaderUtils;

/**
 * Factory methods for the readers in this package.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReadersFactory {

    /**
     * Creates an {@link EnvironmentVariableReader} to access system variables.
     */
    public static EnvironmentVariableReader createEnvironmentVariableReader() {
        return new EnvironmentVariableReader();
    }

    /**
     * Creates a {@link SystemPropertyReader} to access system properties.
     */
    public static SystemPropertyReader createSystemPropertyReader() {
        return new SystemPropertyReader();
    }

    /**
     * Creates a {@link ResourceFileReader} to access application resources.
     */
    public static ResourceFileReader createResourceFileReader() {
        Function<String, String> resourceAccessor = (String resourceName) -> {
            return ClassLoaderUtils.getResourceAsString(resourceName, ResourceFileReader.class);
        };
        return new ResourceFileReader(resourceAccessor);
    }

}
