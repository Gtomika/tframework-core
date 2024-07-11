package org.tframework.test.elements.filter;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.elements.context.filter.annotation.RequiredProperty;
import org.tframework.test.commons.annotations.SetProperties;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

import static org.tframework.test.commons.utils.TframeworkAssertions.assertHasElement;
import static org.tframework.test.commons.utils.TframeworkAssertions.assertHasNoElement;

@IsolatedTFrameworkTest
@SetProperties({"my.property=value", "my.other.property=otherValue"})
public class PropertiesElementContextFilterTest {

    @Element
    @RequiredProperty(name = "my.property")
    public static class ElementWithRequiredPropertyPresent {}

    @Element
    @RequiredProperty(name = "not.existing.property")
    public static class ElementWithNonExistingRequiredProperty {}

    @Test
    public void shouldFilterElementContextBasedOnPropertyName(@InjectElement ElementsContainer elementsContainer) {
        assertHasElement(elementsContainer, ElementWithRequiredPropertyPresent.class);
        assertHasNoElement(elementsContainer, ElementWithNonExistingRequiredProperty.class);
    }

    @Element
    @RequiredProperty(name = "my.property", hasValue = "value")
    public static class ElementWithRequiredPropertyAndValuePresent {}

    @Element
    @RequiredProperty(name = "my.property", hasValue = "different value")
    public static class ElementWithRequiredPropertyPresentButValueIsMismatched {}

    @Test
    public void shouldFilterElementContextsBasedOnPropertyAndValue(@InjectElement ElementsContainer elementsContainer) {
        assertHasElement(elementsContainer, ElementWithRequiredPropertyAndValuePresent.class);
        assertHasNoElement(elementsContainer, ElementWithRequiredPropertyPresentButValueIsMismatched.class);
    }

    @Element
    @RequiredProperty(name = "my.property", hasNoValue = "different value")
    public static class ElementWithRequiredPropertyAndNoValue {}

    @Element
    @RequiredProperty(name = "my.property", hasNoValue = "value")
    public static class ElementWithRequiredPropertyAndMismatchedNoValue {}

    @Test
    public void shouldFilterElementContextsBasedOnPropertyAndNoValue(@InjectElement ElementsContainer elementsContainer) {
        assertHasElement(elementsContainer, ElementWithRequiredPropertyAndNoValue.class);
        assertHasNoElement(elementsContainer, ElementWithRequiredPropertyAndMismatchedNoValue.class);
    }

    @Element
    @RequiredProperty(name = "my.property", hasValue = "value")
    @RequiredProperty(name = "my.other.property", hasValue = "otherValue")
    public static class ElementWithMultiplePropertyConditions {}

    @Element
    @RequiredProperty(name = "my.property", hasValue = "value")
    @RequiredProperty(name = "my.other.property", hasValue = "differentValue")
    public static class ElementWithMultiplePropertyConditionsButOneNotFulfilled {}

    @Test
    public void shouldFilterElementContextsBasedOnMultiplePropertyConditions(@InjectElement ElementsContainer elementsContainer) {
        assertHasElement(elementsContainer, ElementWithMultiplePropertyConditions.class);
        assertHasNoElement(elementsContainer, ElementWithMultiplePropertyConditionsButOneNotFulfilled.class);
    }
}
