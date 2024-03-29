/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context.filter;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.tframework.core.Application;
import org.tframework.core.elements.ElementsInitializationInput;
import org.tframework.core.profiles.ProfilesContainer;

public class ElementContextFilterFactoryTest {

    @Test
    public void shouldCreateDefaultElementContextFilterAggregator() {
        var input = ElementsInitializationInput.builder()
                .application(
                        Application.builder()
                                .profilesContainer(ProfilesContainer.empty())
                                .build()
                )
                .build();

        var aggregator = ElementContextFilterFactory.createDefaultElementContextFilterAggregator(input);

        assertTrue(aggregator.getFilters().stream().anyMatch(f -> f.getClass().equals(ProfileElementContextFilter.class)));
    }

}
