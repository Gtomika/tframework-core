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
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.elements.context.filter.annotation.ForbiddenProfile;
import org.tframework.core.elements.context.filter.annotation.RequiredProfile;
import org.tframework.test.commons.annotations.SetProfiles;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@SetProfiles({"a", "b"})
@IsolatedTFrameworkTest
public class ProfilesElementContextFilterTest {

    @Element
    @RequiredProfile("a") //required and present profile: element will be kept
    public static class Service1 {}

    @Element
    @RequiredProfile("c") //this profile is required, but missing: element will be discarded
    public static class Service2 {}

    @Element
    @ForbiddenProfile("c") //this profile is forbidden, and not set, so element will be kept
    public static class Service3 {}

    @Element
    @ForbiddenProfile("a") //this profile is set, but the element forbids it: element will be discarded
    public static class Service4 {}

    @Element
    @RequiredProfile({"a", "b"})
    @ForbiddenProfile("c") //all required profiles present, forbidden profiles are absent: element will be kept
    public static class Service5 {}

    @Test
    public void shouldFilterElementsBasedOnProfiles(@InjectElement ElementsContainer elementsContainer) {
        assertHasElement(elementsContainer, Service1.class);
        assertHasNoElement(elementsContainer, Service2.class);
        assertHasElement(elementsContainer, Service3.class);
        assertHasNoElement(elementsContainer, Service4.class);
        assertHasElement(elementsContainer, Service5.class);
    }
}
