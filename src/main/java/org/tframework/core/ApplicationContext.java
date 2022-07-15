/* Licensed under Apache-2.0 2022. */
package org.tframework.core;

import lombok.Getter;
import org.tframework.core.exceptions.TFrameworkRuntimeException;
import org.tframework.core.ioc.TFrameworkIoc;
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
     * @param rootClass The class annotated with {@link org.tframework.core.TFrameworkRoot}.
     */
    protected static void initApplicationContext(Class<?> rootClass) {
        if(instance != null) throw new TFrameworkRuntimeException("Multiple application context initialization is not allowed.");
        instance = new ApplicationContext(rootClass);
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
     * Contains data and operations related to the TFramework IoC.
     */
    @Getter
    private final TFrameworkIoc tFrameworkIoc;

    /**
     * Create application context.
     * @param rootClass The class annotated with {@link org.tframework.core.TFrameworkRoot}.
     */
    private ApplicationContext(Class<?> rootClass) {
        tFrameworkIoc = TFrameworkIoc.createInstance(rootClass);
    }

    public <T> T grab(Class<T> managedEntityType) throws NoSuchManagedEntityException, MultipleManagedEntitiesException {
        return null; //TODO
    }
}
