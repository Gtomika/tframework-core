/* Licensed under Apache-2.0 2024. */
package org.tframework.elements.scanning;

import static org.tframework.test.commons.utils.TframeworkAssertions.assertHasElement;

import org.junit.jupiter.api.Test;
import org.tframework.core.Application;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@IsolatedTFrameworkTest
public class DefaultPreConstructedElementsTest {

    @Test
    public void shouldHaveDefaultPreConstructedElements(@InjectElement ElementsContainer elementsContainer) {
        assertHasElement(elementsContainer, Application.class);
        assertHasElement(elementsContainer, ProfilesContainer.class);
        assertHasElement(elementsContainer, PropertiesContainer.class);
        assertHasElement(elementsContainer, ElementsContainer.class);
    }

}
