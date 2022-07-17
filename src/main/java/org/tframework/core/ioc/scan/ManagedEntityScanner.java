package org.tframework.core.ioc.scan;

import lombok.extern.slf4j.Slf4j;
import org.tframework.core.ApplicationContext;
import org.tframework.core.ioc.IocValidator;
import org.tframework.core.ioc.ManagedEntitiesRepository;
import org.tframework.core.ioc.annotations.Managed;
import org.tframework.core.ioc.containers.AbstractContainer;
import org.tframework.core.ioc.containers.ManagedMultiInstanceContainer;
import org.tframework.core.ioc.containers.ManagedSingletonContainer;
import org.tframework.core.ioc.exceptions.IocException;
import org.tframework.core.ioc.exceptions.NameNotUniqueException;
import org.tframework.core.ioc.exceptions.NotConstructibleException;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Base class for managed entity scanners. The implementations are supposed to find types and
 * methods annotated with {@link Managed}, and register them inside the {@link org.tframework.core.ApplicationContext}s
 * repository of such entities, {@link org.tframework.core.ioc.ManagedEntitiesRepository}.
 * <p>
 * Some common methods are provided here that are useful for every managed entity scanner implementation.
 */
@Slf4j
public abstract class ManagedEntityScanner {

    /**
     * Find and register all managed entities and store them in the {@link org.tframework.core.ioc.ManagedEntitiesRepository}.
     * @param rootClass The root class of the Tframework application. Usually defined by annotation {@link org.tframework.core.TFrameworkRoot}.
     *                  Different implementation of {@link ManagedEntityScanner} can interpret it differently.
     * @throws IocException If the scanning or registering failed.
     */
    public abstract void scanAndRegisterManagedEntities(Class<?> rootClass) throws IocException;

    /**
     * Registers all detected managed entity classes (where a class was annotated with {@link Managed}).
     * @param managedEntityClasses The managed entities.
     * @throws IocException If an exception happened when attempting to register the managed entities.
     */
    protected void registerManagedEntities(Set<Class<?>> managedEntityClasses) throws IocException {
        for(var managedEntityClass: managedEntityClasses) {
            Managed managedAnnotation = managedEntityClass.getAnnotation(Managed.class);
            registerManagedEntity(managedAnnotation, managedEntityClass, null);
        }
    }

    /**
     * Registers all provided managed entities (where a method was annotated with {@link Managed}).
     * @param providerMethods The methods that return ("provide") the managed entities.
     * @throws IocException If an exception happened when attempting to register the provided entities.
     */
    protected void registerProvidedEntities(Set<Method> providerMethods) throws IocException {
        for(Method providerMethod: providerMethods) {
            Managed managedAnnotation = providerMethod.getAnnotation(Managed.class);
            Class<?> managedEntityClass = providerMethod.getReturnType();
            registerManagedEntity(managedAnnotation, managedEntityClass, providerMethod);
        }
    }

    /**
     * Register a managed entity in {@link ManagedEntitiesRepository}.
     * @param managedAnnotation The managed annotation on the singleton.
     * @param managedEntityClass The class of the singleton.
     * @param providerMethod The method that provides the entity. This is null of the entity is
     *                       not provided.
     * @throws IocException If the singleton could not be registered.
     */
    protected <T> void registerManagedEntity(
            Managed managedAnnotation,
            Class<T> managedEntityClass,
            @Nullable Method providerMethod
    ) throws IocException {
        //was a custom name specified? if not, default to class name
        String name = managedAnnotation.name().equals(Managed.DEFAULT_MANAGED_NAME)
                ? managedEntityClass.getName() : managedAnnotation.name();
        IocValidator.validateEntityName(name);
        log.debug("Attempting to register managed entity with name '{}' and type '{}'", name, managedEntityClass.getName());
        //attempt to build and register container
        try {
            AbstractContainer<T> container = null;
            switch (managedAnnotation.managedType()) {
                case SINGLETON:
                    container = new ManagedSingletonContainer<>(
                            name, managedEntityClass, providerMethod
                    );
                    break;
                case MULTI_INSTANCE:
                    container = new ManagedMultiInstanceContainer<>(
                            name, managedEntityClass, providerMethod
                    );
            }
            //application context is not managed yet, need to use getInstance
            ManagedEntitiesRepository managedEntitiesRepository = ApplicationContext.getInstance()
                    .getTFrameworkIoc().getManagedEntitiesRepository();
            managedEntitiesRepository.registerManagedEntityContainer(container);
        } catch (IllegalArgumentException | NotConstructibleException | NameNotUniqueException e) {
            throw new IocException(String.format("Failed to register managed singleton '%s'", managedEntityClass.getName()), e);
        }
    }

    /**
     * Registers the {@link ApplicationContext} class as a manages singleton. This cannot be done
     * with the {@link Managed} annotation, because the context is initialized and used before the
     * managed entity scanning takes place.
     */
    protected void registerApplicationContext() {
        var contextContainer = new ManagedSingletonContainer<>(
                ApplicationContext.class.getName(),
                ApplicationContext.class,
                ApplicationContext.getInstance()
        );
        ApplicationContext.getInstance().getTFrameworkIoc().getManagedEntitiesRepository()
                .registerManagedEntityContainer(contextContainer);
        log.debug("Registered the ApplicationContext as managed singleton.");
    }
}
