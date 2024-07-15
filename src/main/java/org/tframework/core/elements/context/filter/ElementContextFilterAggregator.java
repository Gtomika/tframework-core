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

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.tframework.core.Application;
import org.tframework.core.elements.context.ElementContext;

/**
 * Combines several {@link ElementContextFilter}s and applies them to {@link ElementContext}s.
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ElementContextFilterAggregator {

    private final List<ElementContextFilter> filters;

    /**
     * Applies all {@link ElementContextFilter}s to the provided {@link ElementContext}.
     * @param elementContext The element context to be filtered.
     * @param application The application containing additional data that can be used by the filters.
     * @param round The {@link FilteringRound} that the filters should be applied in.
     * @return True, at least one filter wants to discard the context. False if all filters
     * want to keep the context.
     */
    public boolean discardElementContext(
            @NonNull ElementContext elementContext,
            @NonNull Application application,
            @NonNull FilteringRound round
    ) {
        return filters.stream()
                .filter(filter -> filter.applyInRounds().contains(round))
                .anyMatch(filter -> filter.discardElementContext(elementContext, application));
    }

    /**
     * Creates an aggregator that will use the given list of {@link ElementContextFilter}s.
     */
    public static ElementContextFilterAggregator usingFilters(@NonNull List<ElementContextFilter> filters) {
        return new ElementContextFilterAggregator(filters);
    }
}
