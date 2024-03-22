/* Licensed under Apache-2.0 2024. */
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
import org.tframework.core.elements.context.ElementContext;

@ExtendWith(MockitoExtension.class)
public class ElementContextFilterAggregatorTest {

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
        when(filter1.discardElementContext(elementContext)).thenReturn(false);
        when(filter2.discardElementContext(elementContext)).thenReturn(true);
        assertTrue(aggregator.discardElementContext(elementContext));
    }

    @Test
    public void shouldKeepElementContext_whenAllFiltersKeepIt() {
        when(filter1.discardElementContext(elementContext)).thenReturn(false);
        when(filter2.discardElementContext(elementContext)).thenReturn(false);
        assertFalse(aggregator.discardElementContext(elementContext));
    }
}
