/* Licensed under Apache-2.0 2024. */
package org.tframework.test.elements.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.tframework.test.commons.utils.TframeworkAssertions.assertHasElement;
import static org.tframework.test.commons.utils.TframeworkAssertions.assertHasNoElement;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.tframework.core.Application;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.elements.annotations.Priority;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.context.filter.ElementContextFilter;
import org.tframework.test.PriorityChecker;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@IsolatedTFrameworkTest
public class CustomElementContextFilterTest {

    @Element //normally this element would be active, but it is filtered out by the custom filter
    public static class UndesirableElement {}

    @Element
    public PriorityChecker provideChecker() {
        return new PriorityChecker();
    }

    @Element
    @RequiredArgsConstructor
    public static class CustomFilter implements ElementContextFilter {

        private final PriorityChecker priorityChecker;

        @Override
        public boolean discardElementContext(ElementContext elementContext, Application application) {
            priorityChecker.save(this.getClass().getSimpleName());
            return elementContext.getType().equals(UndesirableElement.class);
        }
    }

    @Element
    @Priority(1000)
    @RequiredArgsConstructor
    public static class HighPrioCustomFilter implements ElementContextFilter {

        private final PriorityChecker priorityChecker;

        @Override
        public boolean discardElementContext(ElementContext elementContext, Application application) {
            priorityChecker.save(this.getClass().getSimpleName());
            return elementContext.getType().equals(UndesirableElement.class);
        }
    }

    @Test
    public void shouldFilterElementsWithCustomFilter(
            @InjectElement ElementsContainer elementsContainer,
            @InjectElement PriorityChecker priorityChecker
    ) {
        assertHasNoElement(elementsContainer, UndesirableElement.class);

        assertHasElement(elementsContainer, CustomFilter.class);
        assertHasElement(elementsContainer, HighPrioCustomFilter.class);

        assertEquals(HighPrioCustomFilter.class.getSimpleName(), priorityChecker.getActualOrder().getFirst());
        assertEquals(CustomFilter.class.getSimpleName(), priorityChecker.getActualOrder().getLast());
    }
}
