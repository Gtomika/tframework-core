package org.tframework.core.ioc;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.tframework.core.ApplicationContext;
import org.tframework.core.ioc.annotations.Managed;
import org.tframework.core.ioc.containers.ManagedSingletonContainer;
import org.tframework.core.ioc.exceptions.IocException;
import org.tframework.core.ioc.exceptions.NameNotUniqueException;
import org.tframework.core.ioc.exceptions.NotConstructibleException;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * Contains methods about finding managed entities (these are annotated by {@link Managed}.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ManagedEntityScanner {

    /**
     * Scans the subpackages of the root class for entities that are to be managed by the TFramework.
     * @param rootClass The only class on the classpath that is annotated with {@link org.tframework.core.TFrameworkRoot}.
     * @throws IocException If an exception happened when attempting to register the managed entities.
     */
    public static void scanAndRegisterManagedEntities(Class<?> rootClass) throws IocException {
        log.info("Scanning for managed entities in package '{}' and subpackages...", rootClass.getPackageName());
        //scan the class path for managed entities
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forPackage(rootClass.getPackageName()))
        );
        //these are the CLASSES annotated by @Managed -> does not include provider fields
        var managedEntityClasses = reflections.getTypesAnnotatedWith(Managed.class);
        log.info("Found {} class entities annotated with @Managed in the subpackages of '{}'",
                managedEntityClasses.size(), rootClass.getPackageName());
        registerManagedEntities(managedEntityClasses);
        //need to find the managed entities defined as provider methods
        //they can only be in managed entities classes
        var providedEntities = reflections.getMethodsAnnotatedWith(Managed.class);
        providedEntities.forEach(pme -> IocValidator.validateProviderMethod(pme, null));
        log.info("Found {} provided entities annotated with @Managed in the subpackages of '{}'",
                providedEntities.size(), rootClass.getPackageName());
        registerProvidedEntities(providedEntities);
        //finally, register the ApplicationContext to make it injectable
        registerApplicationContext();
    }

    /**
     * Registers all detected managed entity classes (where a class was annotated with {@link Managed}).
     * @param managedEntityClasses The managed entities.
     * @throws IocException If an exception happened when attempting to register the managed entities.
     */
    private static void registerManagedEntities(Set<Class<?>> managedEntityClasses) throws IocException {
        for(var managedEntityClass: managedEntityClasses) {
            Managed managedAnnotation = managedEntityClass.getAnnotation(Managed.class);
            switch (managedAnnotation.managedType()) {
                case SINGLETON:
                    registerManagedSingletonEntity(managedAnnotation, managedEntityClass, null);
                    break;
                case MULTI_INSTANCE:
                    //TODO: register multi instance managed entity
                    break;
            }
        }
    }

    /**
     * Registers all provided managed entities (where a method was annotated with {@link Managed}).
     * @param providerMethods The methods that return ("provide") the managed entities.
     * @throws IocException If an exception happened when attempting to register the provided entities.
     */
    private static void registerProvidedEntities(Set<Method> providerMethods) throws IocException {
        for(Method providerMethod: providerMethods) {
            Managed managedAnnotation = providerMethod.getAnnotation(Managed.class);
            Class<?> managedEntityClass = providerMethod.getReturnType();
            switch (managedAnnotation.managedType()) {
                case SINGLETON:
                    registerManagedSingletonEntity(managedAnnotation, managedEntityClass, providerMethod);
                    break;
                case MULTI_INSTANCE:
                    //TODO
                    break;
            }
        }
    }

    /**
     * Register a managed singleton in {@link ManagedEntitiesRepository}.
     * @param managedAnnotation The managed annotation on the singleton.
     * @param managedEntityClass The class of the singleton.
     * @param providerMethod The method that provides the entity. This is null of the entity is
     *                       not provided.
     * @throws IocException If the singleton could not be registered.
     */
    private static <T> void registerManagedSingletonEntity(
            Managed managedAnnotation,
            Class<T> managedEntityClass,
            @Nullable Method providerMethod
    ) throws IocException {
        //was a custom name specified? if not, default to class name
        String name = managedAnnotation.name().equals(Managed.DEFAULT_MANAGED_NAME)
                ? managedEntityClass.getName() : managedAnnotation.name();
        log.debug("Attempting to register managed singleton entity with name '{}' and type '{}'", name, managedEntityClass.getName());
        //attempt to build and register container
        try {
            ManagedSingletonContainer<T> container = new ManagedSingletonContainer<>(
                    IocValidator.validateEntityName(name), managedEntityClass, providerMethod
            );
            //application context is not managed yet, need to use getInstance
            ManagedEntitiesRepository managedEntitiesRepository = ApplicationContext.getInstance()
                    .getTFrameworkIoc().getManagedEntitiesRepository();
            managedEntitiesRepository.registerManagedSingletonContainer(container);
        } catch (IllegalArgumentException | NotConstructibleException | NameNotUniqueException e) {
            throw new IocException(String.format("Failed to register managed singleton '%s'", managedEntityClass.getName()), e);
        }
    }

    /**
     * Registers the {@link ApplicationContext} class as a manages singleton. This cannot be done
     * with the {@link Managed} annotation, because the context is initialized and used before the
     * managed entity scanning takes place.
     */
    private static void registerApplicationContext() {
        var contextContainer = new ManagedSingletonContainer<>(
                ApplicationContext.class.getName(),
                ApplicationContext.class,
                ApplicationContext.getInstance()
        );
        ApplicationContext.getInstance().getTFrameworkIoc().getManagedEntitiesRepository()
                .registerManagedSingletonContainer(contextContainer);
        log.debug("Registered the ApplicationContext as managed singleton.");
    }
}
