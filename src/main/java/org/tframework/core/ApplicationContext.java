/* Licensed under Apache-2.0 2022. */
package org.tframework.core;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.exceptions.TFrameworkRuntimeException;
import org.tframework.core.ioc.TFrameworkIoc;
import org.tframework.core.ioc.annotations.Injected;
import org.tframework.core.ioc.annotations.Managed;
import org.tframework.core.ioc.exceptions.MultipleManagedEntitiesException;
import org.tframework.core.ioc.exceptions.NoSuchManagedEntityException;
import org.tframework.core.properties.PropertyRepository;

import javax.annotation.Nonnull;

/**
 * The singleton application context class, which stores information about the running TFramework
 * application, such as the managed classes.
 * <p>
 * This class will be managed by the framework, as if it was annotated with {@link Managed}. However, it is
 * initialized before the managed classes can be scanner, so cannot be managed by annotation.
 */
@Slf4j
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
     * Contains data and operations related to the TFramework IoC.
     */
    @Getter
    private final TFrameworkIoc tFrameworkIoc;

    /**
     * Stores all application properties.
     */
    @Getter
    private final PropertyRepository propertyRepository;

    /**
     * Create application context. It will be created in an initialized state.
     * @param rootClass The class annotated with {@link org.tframework.core.TFrameworkRoot}.
     */
    private ApplicationContext(Class<?> rootClass) {
        propertyRepository = new PropertyRepository();
        tFrameworkIoc = TFrameworkIoc.createInstance();
        log.info("The application context instance have been created with root class '{}'.", rootClass.getName());
    }

    /**
     * Do not use this constructor. This only exists to "fool" the IoC into believing that there
     * is a way to construct {@link ApplicationContext}. In reality, {@link ApplicationContext} is a
     * special case which is pre-constructed.
     */
    @Injected
    public ApplicationContext() {
        tFrameworkIoc = null;
        propertyRepository = null;
    }

    /**
     * Initializes the constructed {@link ApplicationContext} instance.
     * @param rootClass The class annotated with {@link org.tframework.core.TFrameworkRoot}.
     */
    private void initialize(Class<?> rootClass) {
        //TODO initialize property repository
        tFrameworkIoc.initializeIoc(rootClass);
        log.info("The application context instance have been initialized with root class '{}'.", rootClass.getName());
    }

    public <T> T grab(Class<T> managedEntityType) throws NoSuchManagedEntityException, MultipleManagedEntitiesException {
        return tFrameworkIoc.getManagedEntitiesRepository()
                .grabManagedEntityContainer(managedEntityType).grabInstance();
    }
}
