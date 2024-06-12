/* Licensed under Apache-2.0 2024. */
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
     * @return True, at least one filter wants to discard the context. False if all filters
     * want to keep the context.
     */
    public boolean discardElementContext(@NonNull ElementContext elementContext, @NonNull Application application) {
        return filters.stream()
                .anyMatch(filter -> filter.discardElementContext(elementContext, application));
    }

    /**
     * Creates an aggregator that will use the given list of {@link ElementContextFilter}s.
     */
    public static ElementContextFilterAggregator usingFilters(@NonNull List<ElementContextFilter> filters) {
        return new ElementContextFilterAggregator(filters);
    }
}
