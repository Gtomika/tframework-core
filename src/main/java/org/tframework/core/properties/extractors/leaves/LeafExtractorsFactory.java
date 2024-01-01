/* Licensed under Apache-2.0 2023. */
package org.tframework.core.properties.extractors.leaves;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class for creating {@link LeafExtractor}s. TODO test
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LeafExtractorsFactory {

    /**
     * Creates a list of {@link LeafExtractor}s that should be used during property extraction.
     */
    public static List<LeafExtractor> createLeafExtractors() {
        //the order is IMPORTANT
        return List.of(
                new NullLeafExtractor(),
                new ListLeafExtractor(),
                new DefaultLeafExtractor() //must be the last one
        );
    }

}
