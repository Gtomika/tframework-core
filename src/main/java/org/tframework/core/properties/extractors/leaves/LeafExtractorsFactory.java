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
