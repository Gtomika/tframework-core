/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.constructor;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Factory for {@link ConstructorFilter}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConstructorFiltersFactory {

    /**
     * Creates a {@link ConstructorFilter} that other framework components can use.
     */
    public static ConstructorFilter createDefaultConstructorFilter() {
        return new SimpleConstructorFilter();
    }

}
