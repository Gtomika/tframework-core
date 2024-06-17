/* Licensed under Apache-2.0 2024. */
package org.tframework.test.elements.postprocess;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.tframework.core.Application;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.postprocessing.ElementInstancePostProcessor;
import org.tframework.test.commons.utils.TframeworkAssertions;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@IsolatedTFrameworkTest
public class CustomPostProcessorsTest {

    @Element
    public static class EpicElement {

        @InjectElement
        private Application application;
    }

    @Element
    public static class CustomPostProcessor implements ElementInstancePostProcessor {

        private Application application;

        @Override
        public void postProcessInstance(ElementContext elementContext, Object instance) {
            if(instance instanceof EpicElement epicElement) {
                //this processor is called after field injection, so this will work
                this.application = epicElement.application;
            }
        }
    }

    @Test
    public void shouldInvokeCustomPostProcessor(
            @InjectElement ElementsContainer elementsContainer,
            @InjectElement CustomPostProcessor customPostProcessor,
            @InjectElement Application application
    ) {
        TframeworkAssertions.assertHasElement(elementsContainer, EpicElement.class);
        assertEquals(application, customPostProcessor.application);
    }

}
