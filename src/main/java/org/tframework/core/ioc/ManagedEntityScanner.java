package org.tframework.core.ioc;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.tframework.core.ApplicationContext;
import org.tframework.core.ioc.annotations.Managed;
import org.tframework.core.ioc.exceptions.IocException;
import org.tframework.core.ioc.exceptions.NameNotUniqueException;
import org.tframework.core.ioc.exceptions.NotConstructibleException;

import java.util.Set;

/**
 * Contains methods about finding managed classes.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ManagedEntityScanner {

    /**
     * Scans the classpath for entities that are to be managed by the TFramework.
     * @throws IocException If an exception happened when attempting to register the managed entities.
     */
    public static void scanAndRegisterManagedEntities() throws IocException {
        //scan the class path for managed entities
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forJavaClassPath())
        );
        var managedEntities = reflections.getTypesAnnotatedWith(Managed.class);
        log.debug("Found {} entities annotated with @Managed on the classpath", managedEntities.size());
        registerManagedEntities(managedEntities);
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
            ManagedContainer<T> container = ManagedContainer
                    .<T>builder()
                    .managedType(ManagedType.SINGLETON)
                    .name(IocValidator.validateEntityName(name))
                    .instance(ManagedEntityConstructor.constructManagedEntity(managedEntity))
                    .instanceType(managedEntity)
                    .build();
            //application context is not managed yet, need to use getInstance
            ManagedEntitiesRepository managedEntitiesRepository = ApplicationContext.getInstance().getManagedEntitiesRepository();
            managedEntitiesRepository.registerManagedSingletonContainer(container);
        } catch (IllegalArgumentException | NotConstructibleException | NameNotUniqueException e) {
            throw new IocException(String.format("Failed to register managed singleton '%s'", managedEntity.getName()), e);
        }
    }

}
