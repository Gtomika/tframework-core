/* Licensed under Apache-2.0 2023. */
package org.tframework.core;

import lombok.Getter;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.annotations.PreConstructedElement;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.properties.PropertiesContainer;

/**
 * A bundle of information about a TFramework application.
 */
@Getter
@PreConstructedElement
public class Application {

    private boolean finalized;

    private ProfilesContainer profilesContainer;
    private PropertiesContainer propertiesContainer;
    private ElementsContainer elementsContainer;

    private Application() {
        this.finalized = false;
    }

    /**
    Sets the {@link ProfilesContainer} for this application. This method should only be called by the framework.
     */
    @TFrameworkInternal
    public void setProfilesContainer(ProfilesContainer profilesContainer) {
        checkForFinalization();
        this.profilesContainer = profilesContainer;
    }

    /**
     * Sets the {@link PropertiesContainer} for this application. This method should only be called by the framework.
     */
    @TFrameworkInternal
    public void setPropertiesContainer(PropertiesContainer propertiesContainer) {
        checkForFinalization();
        this.propertiesContainer = propertiesContainer;
    }

    /**
     * Sets the {@link ElementsContainer} for this application. This method should only be called by the framework.
     */
    @TFrameworkInternal
    public void setElementsContainer(ElementsContainer elementsContainer) {
        checkForFinalization();
        this.elementsContainer = elementsContainer;
    }

    /**
     * Finalizes the application. This method should only be called by the framework.
     */
    @TFrameworkInternal
    public void finalizeApplication() {
        checkForFinalization();
        this.finalized = true;
    }

    private void checkForFinalization() {
        if (finalized) {
            throw new IllegalStateException("The application has already been finalized");
        }
    }

    public static Application empty() {
        return new Application();
    }

}
