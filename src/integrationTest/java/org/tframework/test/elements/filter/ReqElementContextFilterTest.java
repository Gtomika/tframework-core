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
package org.tframework.test.elements.filter;

import static org.tframework.test.commons.utils.TframeworkAssertions.assertHasElement;
import static org.tframework.test.commons.utils.TframeworkAssertions.assertHasNoElement;

import org.junit.jupiter.api.Test;
import org.tframework.core.Application;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.elements.context.filter.annotation.ForbiddenProfile;
import org.tframework.core.elements.context.filter.annotation.RequiredElement;
import org.tframework.test.commons.annotations.SetProfiles;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@SetProfiles("cool-profile")
@IsolatedTFrameworkTest
public class ReqElementContextFilterTest {

    //Application element is always present, so this element will not be filtered out
    @Element
    @RequiredElement(type = Application.class)
    public static class ApplicationFollowUp {}

    //should be filtered out, because there is no element with name 'some_non_existing_element'
    @Element
    @RequiredElement(name = "some_non_existing_element")
    public static class NonExistingFollowUp {}

    @Test
    public void shouldFilterElement_whenRequiredElementWasNotFound(
            @InjectElement ElementsContainer elementsContainer
    ) {
        assertHasElement(elementsContainer, ApplicationFollowUp.class);
        assertHasNoElement(elementsContainer, NonExistingFollowUp.class);
    }

    //this element will be filtered out because it is not allowed in the cool-profile
    // the required element filter should not see this element
    @Element
    @ForbiddenProfile("cool-profile")
    public static class NotCoolElement {}

    //this will be filtered out, because 'NotCoolElement' will be missing
    @Element
    @RequiredElement(type = NotCoolElement.class)
    public static class NotCoolElementFollowUp {}

    //filtered out, because the follow up is missing
    @Element
    @RequiredElement(type = NotCoolElementFollowUp.class)
    public static class NotCoolElementFollowUpFollowUp {}

    @Test
    public void shouldFilterElement_whenRequiredElementWasAlreadyFilteredOut(
            @InjectElement ElementsContainer elementsContainer
    ) {
        assertHasNoElement(elementsContainer, NotCoolElement.class);
        assertHasNoElement(elementsContainer, NotCoolElementFollowUp.class);
        assertHasNoElement(elementsContainer, NotCoolElementFollowUpFollowUp.class);
    }
}
