/* Licensed under Apache-2.0 2023. */
package org.tframework.core.initializers;

import java.time.Instant;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.Application;
import org.tframework.core.TFrameworkInternal;
import org.tframework.core.elements.ElementsContainer;
import org.tframework.core.elements.ElementsInitializationInput;
import org.tframework.core.elements.PreConstructedElementData;
import org.tframework.core.profiles.ProfileInitializationInput;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.properties.PropertiesInitializationInput;
import org.tframework.core.utils.TimerUtils;

/**
 * The process that bootstraps the framework is described by this class.
 */
@Slf4j
@TFrameworkInternal
@RequiredArgsConstructor
public class CoreInitializationProcess {

    private final ProfilesCoreInitializer profilesInitializer;
    private final PropertiesCoreInitializer propertiesCoreInitializer;
    private final ElementsCoreInitializer elementsCoreInitializer;

    /**
     * Perform the core initialization.
     * @param coreInput {@link CoreInitializationInput} with all data required to initialize.
     * @return {@link Application} that was initialized.
     * @throws InitializationException If the process failed and the application cannot be started.
     */
    public Application performCoreInitialization(CoreInitializationInput coreInput) {
        log.info("Starting TFramework core initialization...");
        Instant start = Instant.now();

        try {
            Application application = Application.empty();
            application.setName(coreInput.applicationName());
            application.setRootClass(coreInput.rootClass());

            ProfilesContainer profilesContainer = initProfiles(coreInput);
            application.setProfilesContainer(profilesContainer);

            PropertiesContainer propertiesContainer = initProperties(coreInput, profilesContainer);
            application.setPropertiesContainer(propertiesContainer);

            ElementsContainer elementsContainer = initDependencyInjection(
                    application,
                    coreInput.rootClass(),
                    coreInput.preConstructedElementData()
            );
            application.setElementsContainer(elementsContainer);

            application.finalizeApplication();
            log.info("Successfully initialized the application '{}'! Let's get started!", application.getName());
            return application;
        } catch (Exception e) {
            log.error("The core initialization has failed", e);
            throw new InitializationException(e);
        } finally {
            log.info("The core initialization completed in {} ms.", TimerUtils.msBetween(start, Instant.now()));
        }
    }

    private ProfilesContainer initProfiles(CoreInitializationInput coreInput) {
        var profileInput = ProfileInitializationInput.builder()
                .args(coreInput.args())
                .build();
        return profilesInitializer.initialize(profileInput);
    }

    private PropertiesContainer initProperties(CoreInitializationInput coreInput, ProfilesContainer profilesContainer) {
        var propertiesInput = PropertiesInitializationInput.builder()
                .profilesContainer(profilesContainer)
                .cliArgs(coreInput.args())
                .build();
        return propertiesCoreInitializer.initialize(propertiesInput);
    }

    private ElementsContainer initDependencyInjection(
            Application application,
            Class<?> rootClass,
            Set<PreConstructedElementData> preConstructedElementData
    ) {
        var dependencyInjectionInput = ElementsInitializationInput.builder()
                .application(application)
                .rootClass(rootClass)
                .preConstructedElementData(preConstructedElementData)
                .build();
        return elementsCoreInitializer.initialize(dependencyInjectionInput);
    }

}
