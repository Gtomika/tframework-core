/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context.filter;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.ElementsInitializationInput;

public class ElementContextFilterFactoryTest {

    @Test
    public void shouldCreateDefaultElementContextFilterAggregator() {
        var input = ElementsInitializationInput.builder()
                .build();

        var aggregator = ElementContextFilterFactory.createDefaultElementContextFilterAggregator(input);
        //TODO: update when actual filters are added
        assertTrue(aggregator.getFilters().isEmpty());
    }

}
