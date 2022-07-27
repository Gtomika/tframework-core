package org.tframework.core.ioc.scan;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.tframework.core.ApplicationContext;
import org.tframework.core.ioc.IocUtils;
import org.tframework.core.ioc.IocValidator;
import org.tframework.core.ioc.ManagedEntitiesRepository;
import org.tframework.core.ioc.annotations.ManagePreConstructedSingleton;
import org.tframework.core.ioc.annotations.Managed;
import org.tframework.core.ioc.containers.AbstractContainer;
import org.tframework.core.ioc.containers.ManagedMultiInstanceContainer;
import org.tframework.core.ioc.containers.ManagedSingletonContainer;
import org.tframework.core.ioc.exceptions.IocException;
import org.tframework.core.ioc.exceptions.NameNotUniqueException;
import org.tframework.core.ioc.exceptions.NotConstructibleException;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Collection;
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
     * Gets a collection of package or class URLs that will be scanned for managed entities by the scanner.
     * @param rootClass he root class of the Tframework application. Usually defined by annotation {@link org.tframework.core.TFrameworkRoot}.
     *                  Different implementation of {@link ManagedEntityScanner} can interpret it differently.
     */
    public abstract Collection<URL> getScannedUrls(Class<?> rootClass);

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
        String name = IocUtils.getReferencedEntityName(managedEntityClass);
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
     * Registers the pre-constructed singletons annotated with {@link ManagePreConstructedSingleton}.
     * @param preConstructedInstanceClasses Classes annotated with {@link ManagePreConstructedSingleton}.
     * @throws IocException If registering of any entities failed.
     */
    protected void registerPreConstructedSingletons(Set<Class<?>> preConstructedInstanceClasses) throws IocException {
        for(Class<?> clazz: preConstructedInstanceClasses) {
            Object instance = obtainPreConstructedInstance(clazz);
            registerPreConstructedSingleton(instance);
        }
    }

    /**
     * Gets the pre-constructed instance using the {@link ManagePreConstructedSingleton#GET_INSTANCE_METHOD_NAME}
     * method of the class.
     * @param preConstructedInstanceClass The annotated class.
     * @return The instance.
     * @throws IocException If this class has no method with name {@link ManagePreConstructedSingleton#GET_INSTANCE_METHOD_NAME}
     * or it returns incorrect type or throws exception.
     */
    protected Object obtainPreConstructedInstance(Class<?> preConstructedInstanceClass) throws IocException {
        Method instanceGetter = MethodUtils.getMatchingMethod(preConstructedInstanceClass,
                ManagePreConstructedSingleton.GET_INSTANCE_METHOD_NAME);
        if(instanceGetter == null || !Modifier.isStatic(instanceGetter.getModifiers())) {
            throw new IocException(String.format("Pre-constructed singleton '%s': no static method found with name '%s', which has " +
                    "no parameters.", preConstructedInstanceClass.getName(), ManagePreConstructedSingleton.GET_INSTANCE_METHOD_NAME));
        }
        log.debug("Pre-constructed singleton '{}': found expected method '{}'.", preConstructedInstanceClass.getName(),
                ManagePreConstructedSingleton.GET_INSTANCE_METHOD_NAME);
        try {
            instanceGetter.setAccessible(true);
            Object instance = instanceGetter.invoke(null);
            log.debug("Successfully obtained pre-constructed instance of '{}'", preConstructedInstanceClass.getName());
            return instance;
        } catch (Exception e) {
            throw new IocException(String.format("Pre-constructed singleton '%s': failed to get return value from method '%s'.",
                    preConstructedInstanceClass.getName(), ManagePreConstructedSingleton.GET_INSTANCE_METHOD_NAME), e);
        }
    }

    /**
     * Registers a pre-constructed singleton annotated with {@link ManagePreConstructedSingleton}.
     * @param instance The pre-constructed instance.
     * @throws NameNotUniqueException If there is already a managed entity with name.
     */
    protected <T> void registerPreConstructedSingleton(T instance) throws NameNotUniqueException {
        Class<T> clazz = (Class<T>)instance.getClass();
        var container = new ManagedSingletonContainer<T>(
                clazz.getName(),
                clazz,
                instance
        );
        ApplicationContext.getInstance().getTFrameworkIoc().getManagedEntitiesRepository()
                .registerManagedEntityContainer(container);
        log.debug("Registered the '{}' pre constructed singleton as managed entity.", clazz.getName());
    }

    /**
     * Checks a class and determines if it is already registered as a provided managed entity.
     * @param managedEntityClass The class annotated with {@link Managed}.
     * @param providedEntityNames The names of the detected provided entities.
     * @return True if this managed entity is provided.
     */
    protected boolean isDetectedAsProvidedEntity(Class<?> managedEntityClass, Set<String> providedEntityNames) {
        String name = IocUtils.getReferencedEntityName(managedEntityClass);
        return providedEntityNames.contains(name);
    }
}
