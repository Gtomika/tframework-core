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
import static org.mockito.Mockito.when;

import java.util.List;
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
        when(filter1.discardElementContext(elementContext, APPLICATION)).thenReturn(false);
        when(filter2.discardElementContext(elementContext, APPLICATION)).thenReturn(true);
        assertTrue(aggregator.discardElementContext(elementContext, APPLICATION));
    }

    @Test
    public void shouldKeepElementContext_whenAllFiltersKeepIt() {
        when(filter1.discardElementContext(elementContext, APPLICATION)).thenReturn(false);
        when(filter2.discardElementContext(elementContext, APPLICATION)).thenReturn(false);
        assertFalse(aggregator.discardElementContext(elementContext, APPLICATION));
    }
}
