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
        var managedEntities = reflections.getTypesAnnotatedWith(Managed.class);
        log.info("Found {} class entities annotated with @Managed in the subpackages of '{}'",
                managedEntities.size(), rootClass.getPackageName());
        registerManagedEntities(managedEntities);
        //need to find the managed entities defined as provider methods
        //they can only be in managed entities classes
        var providedManagedEntities = reflections.getMethodsAnnotatedWith(Managed.class);
        providedManagedEntities.forEach(IocValidator::validateProviderMethod);
        log.info("Found {} provided entities annotated with @Managed in the subpackages of '{}'",
                providedManagedEntities.size(), rootClass.getPackageName());
        //TODO: register provided managed entities. Somehow we need to save provider method
        //finally, register the ApplicationContext to make it injectable
        registerApplicationContext();
    }

    /**
     * Registers all detected managed entities in {@link ManagedEntitiesRepository}.
     * @param managedEntities The managed entities.
     * @throws IocException If an exception happened when attempting to register the managed entities.
     */
    private static void registerManagedEntities(Set<Class<?>> managedEntities) throws IocException {
        for(var managedEntity: managedEntities) {
            Managed managedAnnotation = managedEntity.getAnnotation(Managed.class);
            switch (managedAnnotation.managedType()) {
                case SINGLETON:
                    registerManagedSingletonEntity(managedAnnotation, managedEntity);
                    break;
                case MULTI_INSTANCE:
                    //TODO: register multi instance managed entity
                    break;
            }
        }
        log.debug("Finished registration of {} managed entities.", managedEntities.size());
    }

    /**
     * Register a managed singleton in {@link ManagedEntitiesRepository}.
     * @param managedAnnotation The managed annotation on the singleton.
     * @param managedEntity The class of the singleton.
     * @throws IocException If the singleton could not be registered.
     */
    private static <T> void registerManagedSingletonEntity(Managed managedAnnotation, Class<T> managedEntity) throws IocException {
        //was a custom name specified? if not, default to class name
        String name = managedAnnotation.name().equals(Managed.DEFAULT_MANAGED_NAME)
                ? managedEntity.getName() : managedAnnotation.name();
        //attempt to build and register container
        try {
            ManagedSingletonContainer<T> container = new ManagedSingletonContainer<>(
                    IocValidator.validateEntityName(name), managedEntity
            );
            //application context is not managed yet, need to use getInstance
            ManagedEntitiesRepository managedEntitiesRepository = ApplicationContext.getInstance().getManagedEntitiesRepository();
            managedEntitiesRepository.registerManagedSingletonContainer(container);
        } catch (IllegalArgumentException | NotConstructibleException | NameNotUniqueException e) {
            throw new IocException(String.format("Failed to register managed singleton '%s'", managedEntity.getName()), e);
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
        ApplicationContext.getInstance().getManagedEntitiesRepository()
                .registerManagedSingletonContainer(contextContainer);
        log.debug("Registered the ApplicationContext as managed singleton.");
    }
}
