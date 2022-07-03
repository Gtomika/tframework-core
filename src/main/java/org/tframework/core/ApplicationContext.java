/* Licensed under Apache-2.0 2022. */
package org.tframework.core;

import lombok.Getter;
import org.tframework.core.exceptions.TFrameworkRuntimeException;
import org.tframework.core.ioc.ManagedEntitiesRepository;
import org.tframework.core.ioc.annotations.Managed;
import org.tframework.core.ioc.exceptions.MultipleManagedEntitiesException;
import org.tframework.core.ioc.exceptions.NoSuchManagedEntityException;

/**
 * The singleton application context class, which stores information about the running TFramework
 * application, such as the managed classes.
 * <p>
 * This class will be managed by the framework, as if it was annotated with {@link Managed}. However, it is
 * initialized before the managed classes can be scanner, so cannot be managed by annotation.
 */
public class ApplicationContext {

    /**
     * Only instance of the application context.
     */
    private static ApplicationContext instance;

    /**
     * Initialize application context {@link #instance}. This must only be called
     * once, at the startup of the application.
     */
    protected static void initApplicationContext() {
        if(instance != null) throw new TFrameworkRuntimeException("Multiple application context initialization is not allowed.");
        instance = new ApplicationContext();
    }

    /**
     * Returns the only instance of application context. Can be used when the managed
     * classes are not yet initialized, but we need the application context anyway. After
     * it is added to the managed classes, inject it or grab it instead of using this method.
     */
    public static ApplicationContext getInstance() {
        return instance;
    }

    /**
     * Stores and handles the managed classes.
     */
    @Getter
    private final ManagedEntitiesRepository managedEntitiesRepository;

    private ApplicationContext() {
        //initialize managed classes, it will be filled later
        managedEntitiesRepository = new ManagedEntitiesRepository();
    }

    public <T> T grab(Class<T> managedEntityType) throws NoSuchManagedEntityException, MultipleManagedEntitiesException {
        return null; //TODO
    }
}
