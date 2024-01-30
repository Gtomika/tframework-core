/* Licensed under Apache-2.0 2023. */
package org.tframework.core;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.annotations.PreConstructedElement;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.properties.PropertiesContainer;

/**
 * A bundle of information about a TFramework application. Includes profiles, properties, elements and other
 * information. You may request these information from the application directly, but it is recommended to use
 * dependency injection instead, where possible.
 */
@Getter
@ToString
@EqualsAndHashCode
@PreConstructedElement
public class Application {

    private boolean finalized;

    private String name;
    private Class<?> rootClass;
    private ProfilesContainer profilesContainer;
    private PropertiesContainer propertiesContainer;
    private ElementsContainer elementsContainer;

    private Application() {
        this.finalized = false;
    }

    /**
     * Sets the name for this application. This method should only be called by the framework.
     */
    @TFrameworkInternal
    public void setName(String name) {
        checkForFinalization();
        this.name = name;
    }

    /**
     * Sets the root class for this application. This method should only be called by the framework.
     */
    @TFrameworkInternal
    public void setRootClass(Class<?> rootClass) {
        checkForFinalization();
        this.rootClass = rootClass;
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

    /**
     * Creates an empty application that has no data assigned. This application is not finalized and
     * data should be set before finalization.
     */
    public static Application empty() {
        return new Application();
    }

    /**
     * Creates an application with the given data. This application is not finalized.
     */
    @Builder
    public static Application from(
            String name,
            Class<?> rootClass,
            ProfilesContainer profilesContainer,
            PropertiesContainer propertiesContainer,
            ElementsContainer elementsContainer
    ) {
        var application = new Application();
        application.name = name;
        application.rootClass = rootClass;
        application.profilesContainer = profilesContainer;
        application.propertiesContainer = propertiesContainer;
        application.elementsContainer = elementsContainer;
        return application;
    }

}
