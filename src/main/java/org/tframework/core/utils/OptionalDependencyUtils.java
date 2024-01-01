/* Licensed under Apache-2.0 2023. */
package org.tframework.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class for optional dependencies.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OptionalDependencyUtils {

    /**
     * Checks if the given optional dependency is available on the classpath.
     * @param optionalDependency The optional dependency to check.
     * @return True if the dependency is available, false otherwise.
     */
    public static boolean isOptionalDependencyAvailable(OptionalDependency optionalDependency) {
        return ClassLoaderUtils.isClassAvailable(optionalDependency.getEssentialClassName(), OptionalDependencyUtils.class);
    }

}
