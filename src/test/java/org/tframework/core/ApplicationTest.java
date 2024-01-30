/* Licensed under Apache-2.0 2024. */
package org.tframework.core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.properties.PropertiesContainer;

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
        assertThrows(IllegalStateException.class, () -> application.setPropertiesContainer(PropertiesContainer.empty()));
        assertThrows(IllegalStateException.class, () -> application.setElementsContainer(ElementsContainer.empty()));
        assertThrows(IllegalStateException.class, application::finalizeApplication);
    }

}
