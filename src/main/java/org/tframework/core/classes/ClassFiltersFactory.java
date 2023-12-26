/* Licensed under Apache-2.0 2023. */
package org.tframework.core.classes;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utilities to construct {@link ClassFilter}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClassFiltersFactory {

    /**
     * Creates a {@link DefaultClassFilter}.
     */
    public static DefaultClassFilter createDefaultClassFilter() {
        return new DefaultClassFilter();
    }

}
