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
package org.tframework.core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.properties.PropertiesContainerFactory;

class ApplicationTest {

    @Test
    public void shouldCreateApplicationAsNonFinalized() {
        Application application = Application.empty();
        assertFalse(application.isFinalized());
    }

    @Test
    public void shouldNotAllowModificationAfterFinalization() {
        Application application = Application.empty();
        application.finalizeApplication();

        assertThrows(IllegalStateException.class, () -> application.setName("test"));
        assertThrows(IllegalStateException.class, () -> application.setRootClass(this.getClass()));
        assertThrows(IllegalStateException.class, () -> application.setProfilesContainer(ProfilesContainer.empty()));
        assertThrows(IllegalStateException.class, () -> application.setPropertiesContainer(PropertiesContainerFactory.empty()));
        assertThrows(IllegalStateException.class, () -> application.setElementsContainer(ElementsContainer.empty()));
        assertThrows(IllegalStateException.class, application::finalizeApplication);
    }

}
