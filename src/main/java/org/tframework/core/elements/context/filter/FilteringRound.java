/*
Copyright 2024 Tamas Gaspar

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
package org.tframework.core.elements.context.filter;

/**
 * Represents the possible rounds of element filtering in which {@link ElementContextFilter}s
 * are applied.
 */
public enum FilteringRound {

    /**
     * Represents the first round of {@link ElementContextFilter}ing where the filters
     * that do not depend on other filters are applied.
     */
    FIRST_ROUND,

    /**
     * Represents the second round of {@link ElementContextFilter}ing where the filters
     * that depend on other filters are applied, after some context may have been filtered
     * out already in the first round.
     */
    SECOND_ROUND,

    /**
     * Represents the third round of {@link ElementContextFilter}ing where the filters
     * that depend on other filters and also the result of their own filtering are applied.
     */
    THIRD_ROUND
}
