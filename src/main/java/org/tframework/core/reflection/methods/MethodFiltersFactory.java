/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.methods;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utilities to construct {@link MethodFilter}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MethodFiltersFactory {

    /**
     * Creates a {@link MethodFilter} that other framework components can use.
     */
    public static MethodFilter createDefaultMethodFilter() {
        return new SimpleMethodFilter();
    }

}
