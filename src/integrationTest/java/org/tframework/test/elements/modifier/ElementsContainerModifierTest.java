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
package org.tframework.test.elements.modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.elements.context.ElementContextFactory;
import org.tframework.core.elements.dependency.resolver.DependencyResolutionInput;
import org.tframework.core.elements.modifier.ElementsContainerModifier;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@Slf4j
@IsolatedTFrameworkTest
public class ElementsContainerModifierTest {

    private static final int STRING_ELEMENT_COUNT = 10;

    @Element
    public static class StringElementsCreatorModifier implements ElementsContainerModifier {

        @Override
        public void modifyElementsContainer(ElementsContainer elementsContainer, DependencyResolutionInput dependencyResolutionInput) {
            for(int i = 0; i < STRING_ELEMENT_COUNT; i++) {
                String name = "string" + i;
                String value = "value" + i;
                var elementContext = ElementContextFactory.singleton(value, name);
                elementsContainer.addElementContext(elementContext);
                log.info("Created element context for string element '{}'", name);
            }
        }
    }

    @InjectElement
    private Map<String, String> stringElements;

    @Test
    public void shouldCreateStringElements() {
        assertEquals(STRING_ELEMENT_COUNT, stringElements.size());
        for(int i = 0; i < STRING_ELEMENT_COUNT; i++) {
            String name = "string" + i;
            String value = "value" + i;
            assertEquals(value, stringElements.get(name));
        }
    }
}
