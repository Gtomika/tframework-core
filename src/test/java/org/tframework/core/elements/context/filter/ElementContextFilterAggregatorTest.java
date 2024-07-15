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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.Application;
import org.tframework.core.elements.context.ElementContext;

@ExtendWith(MockitoExtension.class)
public class ElementContextFilterAggregatorTest {

    private static final Application APPLICATION = Application.builder().build();

    @Mock
    private ElementContext elementContext;

    @Mock
    private ElementContextFilter filter1;

    @Mock
    private ElementContextFilter filter2;

    private ElementContextFilterAggregator aggregator;

    @BeforeEach
    public void setUp() {
        aggregator = ElementContextFilterAggregator.usingFilters(List.of(filter1, filter2));
    }

    @Test
    public void shouldDiscardElementContext_whenAnyFiltersDiscardIt() {
        when(filter1.applyInRound()).thenReturn(Set.of(FilteringRound.FIRST_ROUND));
        when(filter2.applyInRound()).thenReturn(Set.of(FilteringRound.FIRST_ROUND));

        when(filter1.discardElementContext(elementContext, APPLICATION)).thenReturn(false);
        when(filter2.discardElementContext(elementContext, APPLICATION)).thenReturn(true);

        boolean discarded = aggregator.discardElementContext(elementContext, APPLICATION, FilteringRound.FIRST_ROUND);
        assertTrue(discarded);
    }

    @Test
    public void shouldKeepElementContext_whenAllFiltersKeepIt() {
        when(filter1.applyInRound()).thenReturn(Set.of(FilteringRound.FIRST_ROUND));
        when(filter2.applyInRound()).thenReturn(Set.of(FilteringRound.FIRST_ROUND));

        when(filter1.discardElementContext(elementContext, APPLICATION)).thenReturn(false);
        when(filter2.discardElementContext(elementContext, APPLICATION)).thenReturn(false);

        boolean discarded = aggregator.discardElementContext(elementContext, APPLICATION, FilteringRound.FIRST_ROUND);
        assertFalse(discarded);
    }

    @Test
    public void shouldRunOnlyFilters_whenRound() {
        when(filter1.applyInRound()).thenReturn(Set.of(FilteringRound.FIRST_ROUND));
        when(filter2.applyInRound()).thenReturn(Set.of(FilteringRound.SECOND_ROUND));

        when(filter1.discardElementContext(elementContext, APPLICATION)).thenReturn(false);

        boolean discarded = aggregator.discardElementContext(elementContext, APPLICATION, FilteringRound.FIRST_ROUND);

        assertFalse(discarded);
        verify(filter2, never()).discardElementContext(elementContext, APPLICATION);
    }

}
