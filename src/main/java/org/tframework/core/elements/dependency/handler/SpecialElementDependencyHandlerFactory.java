/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.dependency.handler;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpecialElementDependencyHandlerFactory {

    /**
     * Creates a {@link SpecialDependencyHandlerAggregator} with a default list of
     * {@link SpecialElementDependencyHandler}s for the framework to use.
     */
    public static SpecialDependencyHandlerAggregator createDefaultHandlerAggregator() {
        var handlers = List.of(
                new ListDependencyHandler(),
                new ArrayDependencyHandler(),
                new OptionalDependencyHandler(),
                new StringMapDependencyHandler()
        );
        return SpecialDependencyHandlerAggregator.usingHandlers(handlers);
    }
}
