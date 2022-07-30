/* Licensed under Apache-2.0 2022. */
package org.tframework.core;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.exceptions.TFrameworkRuntimeException;
import org.tframework.core.flavors.ActiveFlavorManager;
import org.tframework.core.ioc.TFrameworkIoc;
import org.tframework.core.ioc.annotations.Injected;
import org.tframework.core.ioc.annotations.ManagePreConstructedSingleton;
import org.tframework.core.ioc.exceptions.MultipleManagedEntitiesException;
import org.tframework.core.ioc.exceptions.NoSuchManagedEntityException;
import org.tframework.core.properties.PropertyRepository;
import org.tframework.core.properties.PropertyScanner;

import javax.annotation.Nonnull;

/**
 * The singleton application context class, which stores information about the running TFramework
 * application, such as the managed classes.
 */
@Slf4j
@ManagePreConstructedSingleton
public class ApplicationContext {

    /**
     * Only instance of the application context.
     */
    private static ApplicationContext instance;

    /**
     * Create and initialize application context {@link #instance}. This must only be called
     * once, at the startup of the application.
     * @param rootClass The class annotated with {@link org.tframework.core.TFrameworkRoot}.
     */
    protected static void initApplicationContext(Class<?> rootClass) {
        log.info("Attempting to initialize the application context, root class is '{}'.", rootClass.getName());
        if(instance != null) throw new TFrameworkRuntimeException("Multiple application context initialization is not allowed.");
        instance = new ApplicationContext(rootClass);
        instance.initialize(rootClass);
    }

    /**
     * Returns the only instance of application context. Can be used when the managed
     * classes are not yet initialized, but we need the application context anyway. After
     * it is added to the managed classes, inject it or grab it instead of using this method.
     * @throws TFrameworkRuntimeException If the application context is not yet initialized.
     */
    @Nonnull
    public static ApplicationContext getInstance() throws TFrameworkRuntimeException {
        if(instance == null) throw new TFrameworkRuntimeException("The application context is not yet initialized.");
        return instance;
    }

    //------------ non-static ---------------------------------------------------------------------

    /**
     * Stores active flavors.
     */
    @Getter
    private final ActiveFlavorManager activeFlavorManager;

    /**
     * Stores all application properties.
     */
    @Getter
    private final PropertyRepository propertyRepository;

    /**
     * Contains data and operations related to the TFramework IoC.
     */
    @Getter
    private final TFrameworkIoc tFrameworkIoc;

    /**
     * Create application context. It will be created in an initialized state.
     * @param rootClass The class annotated with {@link org.tframework.core.TFrameworkRoot}.
     */
    private ApplicationContext(Class<?> rootClass) {
        activeFlavorManager = new ActiveFlavorManager();
        propertyRepository = new PropertyRepository();
        //create but do not yet initialize the IoC
        tFrameworkIoc = TFrameworkIoc.createInstance();
        log.info("The application context instance have been created with root class '{}'.", rootClass.getName());
    }

    /**
     * Initializes the constructed {@link ApplicationContext} instance.
     * @param rootClass The class annotated with {@link org.tframework.core.TFrameworkRoot}.
     */
    private void initialize(Class<?> rootClass) {
        //flavors must be read first
        activeFlavorManager.readActiveFlavors();
        //then properties
        PropertyScanner propertyScanner = new PropertyScanner(activeFlavorManager, propertyRepository);
        propertyScanner.readProperties();
        //only then the IoC
        tFrameworkIoc.initializeIoc(rootClass);
        log.info("The application context instance have been initialized with root class '{}'.", rootClass.getName());
    }

    /**
     * Gets a managed entity by type.
     * @param managedEntityType The class of the managed entity.
     * @return Instance of the entity.
     * @throws NoSuchManagedEntityException If there is no managed entity with the specified type.
     * @throws MultipleManagedEntitiesException If there are multiple entities with the specified type.
     */
    public <T> T grab(Class<T> managedEntityType) throws NoSuchManagedEntityException, MultipleManagedEntitiesException {
        return tFrameworkIoc.getManagedEntitiesRepository()
                .grabManagedEntityContainer(managedEntityType).grabInstance();
    }

    /**
     * Gets a managed entity by name.
     * @param managedEntityName The name of the managed entity.
     * @return Instance of the entity.
     * @throws NoSuchManagedEntityException If there is no managed entity with the specified name.
     */
    public <T> T grab(String managedEntityName) throws NoSuchManagedEntityException {
        return (T)tFrameworkIoc.getManagedEntitiesRepository()
                .grabManagedEntityContainer(managedEntityName).grabInstance();
    }
}
