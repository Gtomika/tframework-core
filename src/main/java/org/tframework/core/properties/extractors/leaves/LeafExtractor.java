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

import org.tframework.core.properties.PropertyValue;

/**
 * The node extractor transforms a YAML leaf into a {@link PropertyValue}. A leaf is anything
 * that is not a {@link java.util.Map} of values. For example in the YAML
 * <pre>{@code
 * p1: v1
 * p2: v2
 * p3:
 *   p3-1: v3-1
 *   p3-2: v3-2
 * }</pre>
 * the leaves are {@code p1, p2, p3-1, p3-2}, but not {@code p3}, because that is a map of values itself.
 * @see LeafExtractorsFactory
 */
public interface LeafExtractor {

    /**
     * Extracts the given leaf into a {@link PropertyValue}.
     * @param leaf The leaf to extract: it must match this extractor, that is, {@link #matchesLeaf(Object)} must return {@code true}.
     */
    PropertyValue extractLeaf(Object leaf);

    /**
     * Returns {@code true} if this extractor matches the given leaf and can convert it to a {@link PropertyValue}.
     * @param leaf The leaf to check, which can also be null.
     */
    boolean matchesLeaf(Object leaf);

}
