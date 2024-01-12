/* Licensed under Apache-2.0 2023. */
package org.tframework.core.reflection.classes;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utilities to construct {@link ClassFilter}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClassFiltersFactory {

    /**
     * Creates a {@link ClassFilter} for other framework components.
     */
    public static ClassFilter createDefaultClassFilter() {
        return new SimpleClassFilter();
    }

}
